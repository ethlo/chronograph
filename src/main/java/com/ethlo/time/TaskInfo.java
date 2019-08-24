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

import com.ethlo.time.statistics.DurationPerformanceStatistics;
import com.ethlo.time.statistics.PerformanceStatistics;
import com.ethlo.time.statistics.ThroughputPerformanceStatistics;
import com.ethlo.util.IndexedCollection;
import com.ethlo.util.IndexedCollectionStatistics;
import com.ethlo.util.LongList;

public class TaskInfo
{
    private final String name;
    private final IndexedCollection<Long> data;
    private long taskStartTimestamp;
    private boolean running = false;

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

    public long getTotalInvocations()
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
            running = false;
            return true;
        }
        return false;
    }

    void logTiming(final long ts)
    {
        final long duration = ts - taskStartTimestamp;
        data.add(duration);
    }

    public PerformanceStatistics<Duration> getDurationStatistics()
    {
        final IndexedCollectionStatistics stats = new IndexedCollectionStatistics(data);
        return new DurationPerformanceStatistics(stats, getTotalInvocations(), Duration.ofNanos(stats.sum()));
    }

    public PerformanceStatistics<Double> getThroughputStatistics()
    {
        final IndexedCollectionStatistics stats = new IndexedCollectionStatistics(data);
        return new ThroughputPerformanceStatistics(stats, getTotalInvocations(), Duration.ofNanos(stats.sum()));
    }

    public Duration getTotal()
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
