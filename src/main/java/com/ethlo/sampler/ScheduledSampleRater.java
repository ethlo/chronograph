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
 * A sample rate limiter that minimizes overhead for the executing task thread by scheduling periodic progress updates.
 * Progress is emitted at a fixed interval defined by the user.
 *
 * @param <T> The result type to sample.
 */
public class ScheduledSampleRater<T> extends SampleRater<T>
{
    private final ScheduledExecutorService executor;
    private volatile boolean ready = true;

    /**
     * Constructs a ScheduledSampleRater with the specified sampling interval and listener for progress updates.
     * Uses a single-threaded executor for scheduling.
     *
     * @param interval                The time interval between progress updates.
     * @param sampledProgressListener The listener to receive progress updates.
     */
    public ScheduledSampleRater(Duration interval, final Consumer<TaskProgress<T>> sampledProgressListener)
    {
        this(new ScheduledThreadPoolExecutor(1), interval, sampledProgressListener);
    }

    /**
     * Constructs a ScheduledSampleRater with the specified executor, sampling interval, and listener for progress updates.
     *
     * @param executor                The executor service used for scheduling progress updates.
     * @param interval                The time interval between progress updates.
     * @param sampledProgressListener The listener to receive progress updates.
     */
    public ScheduledSampleRater(final ScheduledExecutorService executor, final Duration interval, final Consumer<TaskProgress<T>> sampledProgressListener)
    {
        super(sampledProgressListener);
        this.executor = executor;
        scheduleNext(interval);
    }

    /**
     * Determines whether progress should be emitted. Progress is emitted once every scheduled interval.
     * Only emits if the sample rate limiter is ready.
     *
     * @param progress The current progress of the task.
     * @return True if progress should be emitted, otherwise false.
     */
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

    /**
     * Schedules the next progress update at the specified interval.
     *
     * @param interval The time interval between updates.
     */
    private void scheduleNext(Duration interval)
    {
        executor.schedule(() -> scheduleNext(interval), interval.toNanos(), TimeUnit.NANOSECONDS);
        ready = true;
    }
}
