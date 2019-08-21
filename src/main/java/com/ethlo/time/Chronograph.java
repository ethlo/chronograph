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
import java.util.function.Function;
import java.util.function.Supplier;

import com.ethlo.ascii.TableTheme;

public class Chronograph
{
    private static TableTheme theme = TableTheme.NONE;
    private static OutputConfig outputConfig = OutputConfig.DEFAULT;

    private final Map<String, TaskInfo> taskInfos;
    private final CaptureConfig config;

    public Chronograph(CaptureConfig config)
    {
        this.config = config;
        taskInfos = new LinkedHashMap<>();
    }

    public static Chronograph create()
    {
        return new Chronograph(CaptureConfig.EXTENDED);
    }

    public void start(String task)
    {
        if (task == null)
        {
            throw new IllegalArgumentException("task cannot be null");
        }

        final TaskInfo taskTiming = taskInfos.computeIfAbsent(task, taskName -> new TaskInfo(taskName, this.config));
        taskTiming.start();
    }

    public void stop()
    {
        final long ts = System.nanoTime();
        taskInfos.values().forEach(task -> task.stopped(ts, true));
    }

    public boolean isAnyRunning()
    {
        return taskInfos.values().stream().anyMatch(TaskInfo::isRunning);
    }

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

    public synchronized void resetAll()
    {
        taskInfos.clear();
    }

    public Duration getElapsedTime(final String task)
    {
        return getTasks(task).getTotal();
    }

    public TaskInfo getTasks(final String task)
    {
        return Optional.ofNullable(taskInfos.get(task)).orElseThrow(() -> new IllegalStateException("Unknown task " + task));
    }

    public List<TaskInfo> getTasks()
    {
        return Collections.unmodifiableList(new ArrayList<>(taskInfos.values()));
    }

    public boolean isRunning(String task)
    {
        final TaskInfo taskInfo = taskInfos.get(task);
        return taskInfo != null && taskInfo.isRunning();
    }

    public String prettyPrint()
    {
        return Report.prettyPrint(this, outputConfig, theme);
    }

    public Duration getTotalTime()
    {
        return Duration.ofNanos(taskInfos.values().stream().map(TaskInfo::getTotal).map(Duration::toNanos).reduce(0L, Long::sum));
    }

    public void timed(final String taskName, final Runnable task)
    {
        try
        {
            start(taskName);
            task.run();
        } finally
        {
            stop(taskName);
        }
    }

    public <R, T> R timedFunction(final String taskName, final Function<T, R> task, T input)
    {
        try
        {
            start(taskName);
            return task.apply(input);
        } finally
        {
            stop(taskName);
        }
    }

    public <R, T> R timedSupplier(final String taskName, final Supplier<R> task)
    {
        try
        {
            start(taskName);
            return task.get();
        } finally
        {
            stop(taskName);
        }
    }

    public String prettyPrint(final String title)
    {
        return Report.prettyPrint(this, outputConfig.begin().title(title).build(), theme);
    }

    public static void configure(TableTheme theme, OutputConfig outputConfig)
    {
        Chronograph.theme = Objects.requireNonNull(theme, "theme cannot be null");
        Chronograph.outputConfig = Objects.requireNonNull(outputConfig, "outputConfig cannot be null");
    }

    public static void configure(final TableTheme theme)
    {
        Chronograph.theme = Objects.requireNonNull(theme, "theme cannot be null");
    }
}
