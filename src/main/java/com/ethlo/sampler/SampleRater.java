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

import com.ethlo.sampler.TaskProgress;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class SampleRater<T>
{
    private T lastProgress;
    private long lastUpdated;

    protected final Consumer<TaskProgress<T>> sampledProgressListener;

    protected SampleRater(final Consumer<TaskProgress<T>> sampledProgressListener)
    {
        this.sampledProgressListener = Objects.requireNonNull(sampledProgressListener);
    }

    public final void update(T progress)
    {
        if (shouldEmit(progress))
        {
            sampledProgressListener.accept(new TaskProgress<>(lastProgress, progress, lastUpdated > 0 ? Duration.ofNanos(System.nanoTime() - lastUpdated) : Duration.ZERO));
            lastProgress = progress;
            lastUpdated = System.nanoTime();
        }
    }

    protected abstract boolean shouldEmit(T progress);
}
