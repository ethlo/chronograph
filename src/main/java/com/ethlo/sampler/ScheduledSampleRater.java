package com.ethlo.sampler;

/*-
 * #%L
 * Chronograph
 * %%
 * Copyright (C) 2019 - 2023 Morten Haraldsen (ethlo)
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
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * A sample rate limiter that is focusing on minimum overhead for the executing task thread to sample values
 *
 * @param <T> The result type to sample
 */
public class ScheduledSampleRater<T> extends SampleRater<T>
{
    private final ScheduledExecutorService executor;
    private volatile boolean ready = true;

    public ScheduledSampleRater(final Consumer<TaskProgress<T>> sampledProgressListener)
    {
        this(new ScheduledThreadPoolExecutor(1), Duration.ofSeconds(1), sampledProgressListener);
    }

    public ScheduledSampleRater(Duration interval, final Consumer<TaskProgress<T>> sampledProgressListener)
    {
        this(new ScheduledThreadPoolExecutor(1), interval, sampledProgressListener);
    }

    public ScheduledSampleRater(final ScheduledExecutorService executor, final Duration interval, final Consumer<TaskProgress<T>> sampledProgressListener)
    {
        super(sampledProgressListener);
        this.executor = executor;
        scheduleNext(interval);
    }

    @Override
    protected boolean shouldEmit(final T progress)
    {
        if (ready)
        {
            ready = false;
            return true;
        }
        return false;
    }

    private void scheduleNext(Duration interval)
    {
        executor.schedule(() -> scheduleNext(interval), interval.toNanos(), TimeUnit.NANOSECONDS);
        ready = true;
    }
}
