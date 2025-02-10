package com.ethlo.time.context;

/*-
 * #%L
 * Chronograph
 * %%
 * Copyright (C) 2019 - 2025 Morten Haraldsen (ethlo)
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

import com.ethlo.Beta;
import com.ethlo.time.CaptureConfig;
import com.ethlo.time.Chronograph;
import com.ethlo.time.OutputConfig;

/**
 * Manages thread-local instances of {@link Chronograph}, ensuring that each thread
 * has its own separate instance. Provides configuration options for output and capture settings.
 */
@Beta
public class ChronographContext
{
    private final Map<Thread, Chronograph> instances = new WeakHashMap<>();
    private OutputConfig outputConfig;
    private CaptureConfig captureConfig;


    /**
     * Create a context with default capture and output config
     */
    public ChronographContext()
    {

    }

    /**
     * Retrieves the {@link Chronograph} instance for the current thread. If no instance exists,
     * a new one is created using the current {@link CaptureConfig} settings.
     *
     * @return The {@link Chronograph} instance for the current thread.
     */
    public Chronograph get()
    {
        synchronized (instances)
        {
            return instances.computeIfAbsent(Thread.currentThread(), k -> Chronograph.create(getCaptureConfig()));
        }
    }

    /**
     * Removes the {@link Chronograph} instance associated with the current thread.
     */
    public void remove()
    {
        synchronized (instances)
        {
            instances.remove(Thread.currentThread());
        }
    }

    /**
     * Retrieves the current output configuration. If no custom configuration is set,
     * the default {@link OutputConfig#DEFAULT} is returned.
     *
     * @return The current {@link OutputConfig}.
     */
    public OutputConfig getOutputConfig()
    {
        return Optional.ofNullable(outputConfig).orElse(OutputConfig.DEFAULT);
    }

    /**
     * Sets a custom output configuration.
     *
     * @param outputConfig The output configuration to set.
     * @return The updated {@link ChronographContext} instance.
     */
    public ChronographContext setOutputConfig(final OutputConfig outputConfig)
    {
        this.outputConfig = outputConfig;
        return this;
    }

    /**
     * Retrieves the current capture configuration. If no custom configuration is set,
     * the default {@link CaptureConfig#DEFAULT} is returned.
     *
     * @return The current {@link CaptureConfig}.
     */
    public CaptureConfig getCaptureConfig()
    {
        return Optional.ofNullable(captureConfig).orElse(CaptureConfig.DEFAULT);
    }

    /**
     * Sets a custom capture configuration.
     *
     * @param captureConfig The capture configuration to set.
     * @return The updated {@link ChronographContext} instance.
     */
    public ChronographContext setCaptureConfig(final CaptureConfig captureConfig)
    {
        this.captureConfig = captureConfig;
        return this;
    }

    /**
     * Returns all available {@link Chronograph} instances known across all threads. Useful for
     * outputting data after a multithreaded test, for example.
     *
     * @return A list of all known {@link Chronograph} instances across all threads.
     */
    public List<Chronograph> getAll()
    {
        return new ArrayList<>(instances.values());
    }
}
