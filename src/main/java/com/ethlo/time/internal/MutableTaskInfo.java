package com.ethlo.time.internal;

/*-
 * #%L
 * stopwatch
 * %%
 * Copyright (C) 2019 Morten Haraldsen (ethlo)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import com.ethlo.time.TaskInfo;
import com.ethlo.time.statistics.PerformanceStatistics;
import com.ethlo.time.internal.util.IndexedCollection;
import com.ethlo.time.internal.util.IndexedCollectionStatistics;
import com.ethlo.time.internal.util.LongList;

public class MutableTaskInfo implements TaskInfo
{
    private final IndexedCollection<Long> data;
    private final String name;
    private final MutableTaskInfo parent;
    private final List<MutableTaskInfo> children = new ArrayList<>(0);
    protected boolean running = false;
    private long taskStartTimestamp;

    public MutableTaskInfo(final String name, final MutableTaskInfo parent)
    {
        this(name, parent, new LongList());
    }

    public MutableTaskInfo(String name, final MutableTaskInfo parent, IndexedCollection<Long> data)
    {
        this.name = name;
        this.parent = parent;
        this.data = data;
        if (parent != null)
        {
            parent.children.add(this);
        }
    }

    public boolean start()
    {
        if (!running)
        {
            running = true;
            taskStartTimestamp = System.nanoTime();
            return true;
        }
        return false;
    }

    public boolean stopped(final long ts)
    {
        if (running)
        {
            final long duration = ts - taskStartTimestamp;
            logElapsedDuration(duration);
            running = false;
            return true;
        }
        return false;
    }

    public Duration getTime()
    {
        return Duration.ofNanos(data.stream().reduce(0L, Long::sum));
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public long getInvocations()
    {
        return data.size();
    }

    @Override
    public long getSampleSize()
    {
        return data.size();
    }

    public boolean isRunning()
    {
        return running;
    }

    void logElapsedDuration(final long duration)
    {
        data.add(duration);
    }

    public PerformanceStatistics getStatistics()
    {
        final IndexedCollectionStatistics stats = new IndexedCollectionStatistics(data);
        return new PerformanceStatistics(stats, getInvocations(), getTime().toNanos());
    }

    public long getTaskStartTimestamp()
    {
        return taskStartTimestamp;
    }

    public IndexedCollection<Long> getData()
    {
        return data;
    }

    public void addChild(MutableTaskInfo newTask)
    {
        children.add(newTask);
    }

    @Override
    public Duration getSelfTime()
    {
        final Duration totalTime = getTime();
        final Duration subTaskTime = getSubtasksTime();
        return totalTime.minus(subTaskTime);
    }

    @Override
    public Duration getSubtasksTime()
    {
        return children.stream()
                .map(MutableTaskInfo::getTime)
                .reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public int getDepth()
    {
        int depth = 0;
        MutableTaskInfo current = this;
        while (current.parent != null)
        {
            depth++;
            current = current.parent;
        }
        return depth;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MutableTaskInfo taskInfo = (MutableTaskInfo) object;
        return Objects.equals(name, taskInfo.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ", MutableTaskInfo.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .toString();
    }

    public List<TaskInfo> getSubtasks()
    {
        return new ArrayList<>(children);
    }

    public MutableTaskInfo getParent()
    {
        return parent;
    }

    public void merge(MutableTaskInfo other)
    {
        if (other == null)
        {
            throw new IllegalArgumentException("Cannot merge with null TaskInfo.");
        }

        // Merge task times (sum the durations)
        this.data.addAll(other.getData()); // Add all data entries from the other TaskInfo

        // Merge the children
        for (TaskInfo child : other.getSubtasks())
        {
            // Check if the child task already exists in the current task's children
            Optional<MutableTaskInfo> existingChildOpt = this.children.stream()
                    .filter(existingChild -> existingChild.getName().equals(child.getName()))
                    .findFirst();

            if (existingChildOpt.isPresent())
            {
                // Merge the data entries for the existing child
                MutableTaskInfo existingChild = existingChildOpt.get();
                existingChild.mergeData(child);  // Merge data for this specific child task
            }
            else
            {
                // If the child doesn't exist, add the child task
                this.addChild((MutableTaskInfo) child);
            }
        }
    }

    private void mergeData(TaskInfo other)
    {
        if (other == null)
        {
            return; // If the other task is null, nothing to merge
        }

        // Merge the task data (elapsed times)
        this.data.addAll(((MutableTaskInfo) other).getData());
    }

    public void addMeasurement(long sample)
    {
        this.data.add(sample);
    }
}
