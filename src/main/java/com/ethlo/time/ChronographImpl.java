package com.ethlo.time;

/*-
 * #%L
 * stopwatch
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.ethlo.ascii.TableTheme;

public class ChronographImpl implements Chronograph
{
    private static TableTheme theme = TableTheme.DEFAULT;
    private static OutputConfig outputConfig = OutputConfig.DEFAULT;

    private final CaptureConfig captureConfig = CaptureConfig.builder().sampleRate(Duration.ofMillis(10)).build();

    private final Map<String, TaskInfo> taskInfos;

    public ChronographImpl()
    {
        taskInfos = new LinkedHashMap<>();
    }

    public static void configure(TableTheme theme, OutputConfig outputConfig)
    {
        configure(theme);
        configure(outputConfig);
    }

    public static void configure(final TableTheme theme)
    {
        ChronographImpl.theme = Objects.requireNonNull(theme, "theme cannot be null");
    }

    public static void configure(final OutputConfig outputConfig)
    {
        ChronographImpl.outputConfig = Objects.requireNonNull(outputConfig, "outputConfig cannot be null");
    }

    @Override
    public void start(String task)
    {
        if (task == null)
        {
            throw new IllegalArgumentException("task cannot be null");
        }

        final TaskInfo taskInfo = taskInfos.computeIfAbsent(task, TaskInfo::new);
        taskInfo.start();
    }

    @Override
    public void stop()
    {
        final long ts = System.nanoTime();
        taskInfos.values().forEach(task -> task.stopped(ts, true));
    }

    @Override
    public boolean isAnyRunning()
    {
        return taskInfos.values().stream().anyMatch(TaskInfo::isRunning);
    }

    @Override
    public void stop(String task)
    {
        final long ts = System.nanoTime();
        final TaskInfo taskInfo = taskInfos.get(task);
        if (taskInfo == null)
        {
            throw new IllegalStateException("No started task with name " + task);
        }
        taskInfo.stopped(ts, false);
    }

    @Override
    public void resetAll()
    {
        taskInfos.clear();
    }

    @Override
    public TaskInfo getTasks(final String task)
    {
        return Optional.ofNullable(taskInfos.get(task)).orElseThrow(() -> new IllegalStateException("Unknown task " + task));
    }

    @Override
    public List<TaskInfo> getTasks()
    {
        return Collections.unmodifiableList(new ArrayList<>(taskInfos.values()));
    }

    @Override
    public boolean isRunning(String task)
    {
        final TaskInfo taskInfo = taskInfos.get(task);
        return taskInfo != null && taskInfo.isRunning();
    }

    public String prettyPrint()
    {
        return Report.prettyPrint(this, outputConfig, theme);
    }

    @Override
    public Duration getTotalTime()
    {
        return Duration.ofNanos(taskInfos.values().stream().map(TaskInfo::getTotal).map(Duration::toNanos).reduce(0L, Long::sum));
    }
}
