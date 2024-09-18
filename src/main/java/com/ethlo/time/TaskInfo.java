package com.ethlo.time;

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

import com.ethlo.time.statistics.PerformanceStatistics;
import com.ethlo.util.IndexedCollection;
import com.ethlo.util.IndexedCollectionStatistics;
import com.ethlo.util.LongList;

public class TaskInfo
{
    private final String name;
    private final IndexedCollection<Long> data;
    protected boolean running = false;
    private long taskStartTimestamp;

    TaskInfo(final String name)
    {
        this.name = name;
        this.data = new LongList();
    }

    long start()
    {
        if (running)
        {
            throw new IllegalStateException("Task " + name + " is already started");
        }
        running = true;

        // The very last operation
        taskStartTimestamp = System.nanoTime();
        return taskStartTimestamp;
    }

    public String getName()
    {
        return name;
    }

    public long getTotalTaskInvocations()
    {
        return data.size();
    }

    public long getSampleSize()
    {
        return data.size();
    }

    public boolean isRunning()
    {
        return running;
    }

    boolean stopped(final long ts, boolean ignoreState)
    {
        if (!running && !ignoreState)
        {
            throw new IllegalStateException("Task " + name + " is not started");
        }

        if (running)
        {
            logElapsedDuration(ts - getTaskStartTimestamp());
            running = false;
            return true;
        }
        return false;
    }

    void logElapsedDuration(final long duration)
    {
        data.add(duration);
    }

    public PerformanceStatistics getDurationStatistics()
    {
        final IndexedCollectionStatistics stats = new IndexedCollectionStatistics(data);
        return new PerformanceStatistics(stats, getTotalTaskInvocations(), stats.sum());
    }

    public Duration getTotalTaskTime()
    {
        return Duration.ofNanos(data.stream().reduce(0L, Long::sum));
    }

    protected long getTaskStartTimestamp()
    {
        return taskStartTimestamp;
    }

    protected IndexedCollection<Long> getData()
    {
        return data;
    }
}
