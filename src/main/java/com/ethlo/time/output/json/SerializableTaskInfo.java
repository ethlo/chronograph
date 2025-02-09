package com.ethlo.time.output.json;

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

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ethlo.time.OutputConfig;
import com.ethlo.time.TaskInfo;
import com.ethlo.time.statistics.PerformanceStatistics;

public class SerializableTaskInfo implements Serializable
{
    private final String name;
    private final List<SerializableTaskInfo> subtasks;
    private final Duration totalTime;
    private final Duration subtasksTime;
    private final Duration selfTime;
    private final long invocationCount;
    private final SerializableTaskStatistics statistics;

    public SerializableTaskInfo(String name, List<SerializableTaskInfo> subtasks, Duration totalTime, Duration subtasksTime, final Duration selfTime, final long invocationCount, SerializableTaskStatistics taskStatistics)
    {
        this.name = name;
        this.subtasks = subtasks;
        this.totalTime = totalTime;
        this.subtasksTime = subtasksTime;
        this.selfTime = selfTime;
        this.invocationCount = invocationCount;
        this.statistics = taskStatistics;
    }

    public static SerializableTaskInfo create(TaskInfo source, OutputConfig config)
    {
        final List<SerializableTaskInfo> processed = new ArrayList<>();
        processChildren(source.getSubtasks(), processed, config);

        SerializableTaskStatistics taskStatistics = null;
        if (source.getInvocations() > 1)
        {
            final PerformanceStatistics statistics = source.getStatistics();
            final Map<Double, Duration> percentiles = Arrays.stream(config.percentiles()).boxed()
                    .collect(Collectors.toMap(l -> l, statistics::getPercentile));
            taskStatistics = new SerializableTaskStatistics(statistics.getAverage(), statistics.getMedian(),
                    statistics.getMin(), statistics.getMax(), statistics.getStandardDeviation(), percentiles
            );
        }
        return new SerializableTaskInfo(source.getName(), processed, source.getTime(), source.getSubtasksTime(), source.getSelfTime(), source.getInvocations(), taskStatistics);
    }

    private static void processChildren(List<TaskInfo> children, final List<SerializableTaskInfo> processed, OutputConfig outputConfig)
    {
        for (TaskInfo child : children)
        {
            processed.add(create(child, outputConfig));
        }
    }

    public String getName()
    {
        return name;
    }

    public List<SerializableTaskInfo> getSubtasks()
    {
        return Optional.ofNullable(subtasks).filter(l -> !l.isEmpty()).orElse(null);
    }

    public Duration getTotalTime()
    {
        return totalTime;
    }

    public Duration getSubtasksTime()
    {
        return subtasksTime;
    }

    public Duration getSelfTime()
    {
        return selfTime;
    }

    public long getInvocationCount()
    {
        return invocationCount;
    }

    public SerializableTaskStatistics getStatistics()
    {
        return statistics;
    }

    public record SerializableTaskStatistics(Duration average, Duration median, Duration min, Duration max,
                                             Duration standardDeviation, Map<Double, Duration> percentiles)
    {

    }
}
