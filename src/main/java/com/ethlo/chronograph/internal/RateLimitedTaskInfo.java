package com.ethlo.chronograph.internal;

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
import java.util.concurrent.ScheduledExecutorService;

import com.ethlo.chronograph.internal.util.IndexedCollectionStatistics;
import com.ethlo.chronograph.statistics.PerformanceStatistics;
import com.ethlo.sampler.SampleRater;
import com.ethlo.sampler.ScheduledSampleRater;

public class RateLimitedTaskInfo extends MutableTaskInfo
{
    private final SampleRater<Long> sampleRater;
    private int totalInvocations;
    private long totalElapsed;

    public RateLimitedTaskInfo(final String name, Duration minInterval, final ScheduledExecutorService scheduledExecutorService, final MutableTaskInfo parent)
    {
        super(name, parent);
        this.sampleRater = new ScheduledSampleRater<>(scheduledExecutorService, minInterval, prg -> logElapsedDuration(prg.progress()));
    }

    @Override
    public boolean stopped(final long ts)
    {
        if (!isRunning())
        {
            throw new IllegalStateException("Task " + getName() + " is not started");
        }

        final long elapsed = ts - super.getTaskStartTimestamp();
        totalInvocations++;
        totalElapsed += elapsed;
        sampleRater.update(elapsed);
        running = false;

        return false;
    }

    @Override
    public long getInvocations()
    {
        return totalInvocations;
    }

    @Override
    public Duration getTime()
    {
        return Duration.ofNanos(totalElapsed);
    }

    @Override
    public PerformanceStatistics getStatistics()
    {
        final IndexedCollectionStatistics stats = new IndexedCollectionStatistics(getData());
        return new PerformanceStatistics(stats, totalInvocations, totalElapsed);
    }
}
