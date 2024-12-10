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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ethlo.time.statistics.PerformanceStatistics;

public class ChronographData
{
    private final String name;
    private final List<TaskPerformanceStatistics> taskStatistics;
    private final Duration totalTime;

    public ChronographData(final String name, final List<TaskPerformanceStatistics> taskStatistics)
    {
        this.name = name;
        this.taskStatistics = taskStatistics;
        this.totalTime = Duration.ofNanos(taskStatistics.stream().mapToLong(t -> t.performanceStatistics().getElapsedTotal().toNanos()).sum());
    }

    public static ChronographData combine(final String name, final List<Chronograph> toCombine)
    {
        if (toCombine.isEmpty())
        {
            throw new IllegalArgumentException("No results to combine");
        }
        if (toCombine.size() == 1)
        {
            return toCombine.get(0).getTaskData();
        }
        else
        {
            ChronographData last = toCombine.get(0).getTaskData();
            for (int i = 1; i < toCombine.size(); i++)
            {
                last = last.merge(name, toCombine.get(i).getTaskData());
            }
            return last;
        }
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

    public ChronographData merge(String chronographName, ChronographData chronographData)
    {
        final Map<String, TaskPerformanceStatistics> joined = new LinkedHashMap<>(Math.max(this.taskStatistics.size(), chronographData.taskStatistics.size()));
        this.taskStatistics.forEach(t -> joined.put(t.name(), t));
        chronographData.taskStatistics.forEach(t -> joined.compute(t.name(), (k, v) ->
        {
            if (v != null)
            {
                final long sampleSize = v.sampleSize() + t.sampleSize();
                final PerformanceStatistics durationStatistics = t.performanceStatistics().merge(v.performanceStatistics());
                return new TaskPerformanceStatistics(k, sampleSize, durationStatistics);
            }
            return t;
        }));
        return new ChronographData(chronographName, new ArrayList<>(joined.values()));
    }
}
