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
import java.util.LinkedList;
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

    public static ChronographData combine(final String name, final List<Chronograph> toCombine)
    {
        final List<TaskPerformanceStatistics> all = new LinkedList<>();
        for (Chronograph c : toCombine)
        {
            final ChronographData taskData = c.getTaskData();
            final String runName = taskData.name;

            for (TaskPerformanceStatistics taskStats : taskData.taskStatistics)
            {
                final String fullName = runName != null && runName.length() > 0 ? (runName + " - " + taskStats.getName()) : taskStats.getName();
                all.add(new TaskPerformanceStatistics(fullName, taskStats.getSampleSize(), taskStats.getDurationStatistics(), taskStats.getThroughputStatistics()));
            }
        }
        final Duration total = toCombine.stream().map(Chronograph::getTaskData).map(ChronographData::getTotalTime).reduce(Duration.ZERO, Duration::plus);
        return new ChronographData(name, all, total);
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
