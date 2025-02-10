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
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Abstract class that rates the progress of a task by emitting progress updates based on custom criteria.
 * Subclasses must implement the {@link #shouldEmit(Object)} method to define when progress should be sampled.
 *
 * @param <T> The type representing the progress of the task.
 */
public abstract class SampleRater<T>
{
    protected final Consumer<TaskProgress<T>> sampledProgressListener;
    private T lastProgress;
    private long lastUpdated;

    /**
     * Constructs a SampleRater with a listener that receives sampled task progress updates.
     *
     * @param sampledProgressListener The listener to receive task progress updates.
     */
    protected SampleRater(final Consumer<TaskProgress<T>> sampledProgressListener)
    {
        this.sampledProgressListener = Objects.requireNonNull(sampledProgressListener);
    }

    /**
     * Updates the progress and emits a progress update if the sampling criteria are met.
     * The listener receives the progress along with the time elapsed since the last update.
     *
     * @param progress The current progress of the task.
     */
    public final void update(T progress)
    {
        if (shouldEmit(progress))
        {
            sampledProgressListener.accept(new TaskProgress<>(lastProgress, progress, lastUpdated > 0 ? Duration.ofNanos(System.nanoTime() - lastUpdated) : Duration.ZERO));
            lastProgress = progress;
            lastUpdated = System.nanoTime();
        }
    }

    /**
     * Determines whether progress should be sampled and emitted. This method must be implemented by subclasses
     * to define the sampling logic.
     *
     * @param progress The current progress of the task.
     * @return True if the progress should be emitted, otherwise false.
     */
    protected abstract boolean shouldEmit(T progress);
}
