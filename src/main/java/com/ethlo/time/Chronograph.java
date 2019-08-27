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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.ethlo.ascii.TableTheme;

public class Chronograph
{
    private static TableTheme theme = TableTheme.DEFAULT;
    private static OutputConfig outputConfig = OutputConfig.DEFAULT;

    private final Map<String, TaskInfo> taskInfos;
    private final CaptureConfig captureConfig;
    private final String name;

    public Chronograph(final String name)
    {
        this(name, CaptureConfig.DEFAULT);
    }

    public Chronograph(final CaptureConfig captureConfig)
    {
        this("", captureConfig);
    }

    public Chronograph(final String name, final CaptureConfig captureConfig)
    {
        this.taskInfos = new LinkedHashMap<>();
        this.name = name;
        this.captureConfig = captureConfig;
    }

    public static Chronograph create()
    {
        return new Chronograph("");
    }

    public static Chronograph create(final CaptureConfig captureConfig)
    {
        return create("", captureConfig);
    }

    public static Chronograph create(String name)
    {
        return new Chronograph(name);
    }

    public static Chronograph create(String name, final CaptureConfig captureConfig)
    {
        return new Chronograph(name, captureConfig);
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
        return Report.prettyPrint(this.getTaskData(), outputConfig.title(title), theme);
    }

    public String prettyPrint()
    {
        return Report.prettyPrint(this.getTaskData(), outputConfig, theme);
    }

    public void start(String task)
    {
        if (task == null)
        {
            throw new IllegalArgumentException("task cannot be null");
        }

        final TaskInfo taskInfo = taskInfos.computeIfAbsent(task, t -> captureConfig.getMinInterval().equals(Duration.ZERO) ? new TaskInfo(task) : new RateLimitedTaskInfo(task, captureConfig.getMinInterval()));
        taskInfo.start();
    }

    public void stop()
    {
        final long ts = System.nanoTime();
        for (TaskInfo taskInfo : taskInfos.values())
        {
            final boolean shouldLog = taskInfo.stopped(ts, true);
            if (shouldLog)
            {
                taskInfo.logTiming(ts);
            }
        }
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

        if (taskInfo.stopped(ts, false))
        {
            taskInfo.logTiming(ts);
        }
    }

    public void resetAll()
    {
        taskInfos.clear();
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

    public Duration getTotalTime()
    {
        return Duration.ofNanos(taskInfos.values().stream().map(TaskInfo::getTotalTaskTime).map(Duration::toNanos).reduce(0L, Long::sum));
    }

    public ChronographData getTaskData()
    {
        final List<TaskPerformanceStatistics> stats = getTasks()
                .stream()
                .map(task -> new TaskPerformanceStatistics(task.getName(), task.getSampleSize(), task.getDurationStatistics(), task.getThroughputStatistics()))
                .collect(Collectors.toList());
        return new ChronographData(name, stats, getTotalTime());
    }
}
