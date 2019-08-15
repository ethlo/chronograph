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
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicLong;

class TaskInfo
{
    private final String name;
    private final AtomicLong invocationCounts = new AtomicLong();
    private final AtomicLong totalTaskTime = new AtomicLong();
    private final AtomicLong taskStartTimestamp = new AtomicLong();
    private volatile boolean running = false;

    void started()
    {
        if (running)
        {
            throw new IllegalStateException("Task " + name + " is already started");
        }
        running = true;

        // The very last operation
        taskStartTimestamp.set(System.nanoTime());
    }

    void stopped()
    {
        // The very first operation
        final long ts = System.nanoTime();
        if (!running)
        {
            throw new IllegalStateException("Task " + name + " is not started");
        }
        running = false;

        invocationCounts.incrementAndGet();
        totalTaskTime.addAndGet(ts - taskStartTimestamp.get());
    }

    TaskInfo(final String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public long getInvocationCount()
    {
        return invocationCounts.get();
    }

    public long getTotalTaskTime()
    {
        return totalTaskTime.get();
    }

    public Duration getAverageTaskTime()
    {
        return Duration.ofNanos(BigDecimal.valueOf(getTotalTaskTime()).divide(BigDecimal.valueOf(getInvocationCount()), RoundingMode.HALF_UP).longValue());
    }
}
