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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

import com.ethlo.util.LongList;

public class TaskInfo
{
    private final String name;
    private final LongList data;

    private long invocationCounts;
    private long totalTaskTime;
    private long taskStartTimestamp;
    private boolean running = false;
    private long min = Long.MAX_VALUE;
    private long max = Long.MIN_VALUE;

    TaskInfo(final String name, CaptureConfig config)
    {
        this.name = name;
        this.data = config.storeIndividual() ? new LongList() : null;
    }

    void start()
    {
        if (running)
        {
            throw new IllegalStateException("Task " + name + " is already started");
        }
        running = true;

        // The very last operation
        taskStartTimestamp = System.nanoTime();
    }

    public String getName()
    {
        return name;
    }

    public long getInvocations()
    {
        return invocationCounts;
    }

    public Duration getTotal()
    {
        return Duration.ofNanos(totalTaskTime);
    }

    public Duration getAverage()
    {
        final long invocations = getInvocations();
        if (invocations == 0)
        {
            return Duration.ZERO;
        }

        return Duration.ofNanos(BigDecimal.valueOf(getTotal().toNanos()).divide(BigDecimal.valueOf(getInvocations()), RoundingMode.HALF_UP).longValue());
    }

    public Duration getMedian()
    {
        return data != null ? Duration.ofNanos((long) data.getMedian()) : Duration.ZERO;
    }

    public Duration getPercentile(double limit)
    {
        return data != null ? Duration.ofNanos((long) data.getPercentile(limit)) : Duration.ZERO;
    }

    public boolean isRunning()
    {
        return running;
    }

    void stopped(final long ts, boolean ignoreState)
    {
        if (!running && !ignoreState)
        {
            throw new IllegalStateException("Task " + name + " is not started");
        }

        if (running)
        {
            running = false;

            invocationCounts++;
            final long duration = ts - taskStartTimestamp;
            totalTaskTime += duration;
            min = Math.min(min, duration);
            max = Math.max(max, duration);

            if (data != null)
            {
                data.add(duration);
            }
        }
    }

    public Duration getMin()
    {
        return Duration.ofNanos(min);
    }

    public Duration getMax()
    {
        return Duration.ofNanos(max);
    }

    public Duration getStandardDeviation()
    {
        return data != null ? Duration.ofNanos((long) data.getStandardDeviation()) : Duration.ZERO;
    }
}
