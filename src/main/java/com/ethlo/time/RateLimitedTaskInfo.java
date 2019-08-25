package com.ethlo.time;

/*-
 * #%L
 * Chronograph
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
import com.ethlo.util.IndexedCollectionStatistics;

public class RateLimitedTaskInfo extends TaskInfo
{
    private final long nanosInterval;
    private long lastStopped;
    private int totalInvocations;
    private long totalElapsed;

    RateLimitedTaskInfo(final String name, Duration minInterval)
    {
        super(name);
        this.nanosInterval = minInterval.toNanos();
    }

    @Override
    boolean stopped(final long ts, final boolean ignoreState)
    {
        super.stopped(ts, ignoreState);
        final long elapsed = ts - lastStopped;
        if (lastStopped == 0 || elapsed > nanosInterval)
        {
            lastStopped = ts;
            totalInvocations++;
            totalElapsed += ts - super.getTaskStartTimestamp();
            return true;
        }
        totalInvocations++;
        totalElapsed += ts - super.getTaskStartTimestamp();
        return false;
    }

    @Override
    public long getTotalTaskInvocations()
    {
        return totalInvocations;
    }

    @Override
    public Duration getTotalTaskTime()
    {
        return Duration.ofNanos(totalElapsed);
    }

    @Override
    public PerformanceStatistics<Duration> getDurationStatistics()
    {
        final IndexedCollectionStatistics stats = new IndexedCollectionStatistics(getData());
        return new DurationPerformanceStatistics(stats, totalInvocations, Duration.ofNanos(totalElapsed));
    }
}
