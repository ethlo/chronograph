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
import com.ethlo.ascii.TableTheme;
import com.ethlo.ascii.TableThemes;
import com.ethlo.time.CaptureConfig;
import com.ethlo.time.Chronograph;
import com.ethlo.time.ChronographData;
import com.ethlo.time.OutputConfig;

@Beta
public class ChronographContext
{
    private final Map<Thread, Chronograph> instances = new WeakHashMap<>();
    private OutputConfig outputConfig;
    private CaptureConfig captureConfig;
    private TableTheme theme;

    public Chronograph get()
    {
        synchronized (instances)
        {
            return instances.computeIfAbsent(Thread.currentThread(), k -> Chronograph.create(getCaptureConfig()));
        }
    }

    public void remove()
    {
        synchronized (instances)
        {
            instances.remove(Thread.currentThread());
        }
    }

    public String prettyPrint()
    {
        return get().prettyPrint(getConfig(), getTheme());
    }

    private OutputConfig getConfig()
    {
        return Optional.ofNullable(outputConfig).orElse(OutputConfig.DEFAULT);
    }

    public TableTheme getTheme()
    {
        return Optional.ofNullable(theme).orElse(TableThemes.ASCII);
    }

    public ChronographContext setTheme(final TableTheme theme)
    {
        this.theme = theme;
        return this;
    }

    public OutputConfig getOutputConfig()
    {
        return Optional.ofNullable(outputConfig).orElse(OutputConfig.DEFAULT);
    }

    public ChronographContext setOutputConfig(final OutputConfig outputConfig)
    {
        this.outputConfig = outputConfig;
        return this;
    }

    public CaptureConfig getCaptureConfig()
    {
        return Optional.ofNullable(captureConfig).orElse(CaptureConfig.DEFAULT);
    }

    public ChronographContext setCaptureConfig(final CaptureConfig captureConfig)
    {
        this.captureConfig = captureConfig;
        return this;
    }

    /**
     * Returns all available Chronograph instances known across all threads. Useful for outputting data after
     * a multithreaded test for example.
     *
     * @return All instances known across all threads
     */
    public List<Chronograph> getAll()
    {
        return new ArrayList<>(instances.values());
    }

    /**
     * Merge all the data from multiple instances
     *
     * @param input The instances to merge data for
     * @return The merged result
     */
    public ChronographData merged(List<Chronograph> input)
    {
        ChronographData mergedData = new ChronographData(null, List.of());
        for (Chronograph c : input)
        {
            mergedData = mergedData.merge(c.getTaskData().getName(), c.getTaskData());
        }
        return mergedData;
    }
}
