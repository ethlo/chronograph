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

import com.ethlo.time.statistics.PerformanceStatistics;

public class TaskPerformanceStatistics
{
    private final String name;
    private final PerformanceStatistics<Duration> durationStatistics;
    private final PerformanceStatistics<Double> throughputStatistics;
    private final long sampleSize;

    public TaskPerformanceStatistics(String name, final long sampleSize, final PerformanceStatistics<Duration> durationStatistics, final PerformanceStatistics<Double> throughputStatistics)
    {
        this.name = name;
        this.sampleSize = sampleSize;
        this.durationStatistics = durationStatistics;
        this.throughputStatistics = throughputStatistics;
    }

    public PerformanceStatistics<Duration> getDurationStatistics()
    {
        return durationStatistics;
    }

    public PerformanceStatistics<Double> getThroughputStatistics()
    {
        return throughputStatistics;
    }

    public String getName()
    {
        return name;
    }

    public long getSampleSize()
    {
        return sampleSize;
    }
}
