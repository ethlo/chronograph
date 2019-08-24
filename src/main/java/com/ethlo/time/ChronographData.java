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
import java.util.List;

public class ChronographData
{
    private final String name;
    private final List<TaskPerformanceStatistics> taskStatistics;
    private final Duration totalTime;

    public ChronographData(final String name, final List<TaskPerformanceStatistics> taskStatistics, final Duration totalTime)
    {
        this.name = name;
        this.taskStatistics = taskStatistics;
        this.totalTime = totalTime;
    }

    public String getName()
    {
        return name;
    }

    public List<TaskPerformanceStatistics> getTaskStatistics()
    {
        return taskStatistics;
    }

    public Duration getTotalTime()
    {
        return totalTime;
    }

    public boolean isEmpty()
    {
        return this.taskStatistics.isEmpty();
    }
}
