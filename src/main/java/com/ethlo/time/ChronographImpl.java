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
import java.util.Optional;

public class ChronographImpl extends Chronograph
{
    private final Map<String, TaskInfo> taskInfos;
    private final CaptureConfig captureConfig;

    public ChronographImpl()
    {
        this(CaptureConfig.DEFAULT);
    }

    public ChronographImpl(final CaptureConfig captureConfig)
    {
        this.taskInfos = new LinkedHashMap<>();
        this.captureConfig = captureConfig;
    }

    @Override
    public void start(String task)
    {
        if (task == null)
        {
            throw new IllegalArgumentException("task cannot be null");
        }

        final TaskInfo taskInfo = taskInfos.computeIfAbsent(task, t -> captureConfig.getMinInterval().equals(Duration.ZERO) ? new TaskInfo(task) : new RateLimitedTaskInfo(task, captureConfig.getMinInterval()));
        taskInfo.start();
    }

    @Override
    public void stop()
    {
        final long ts = System.nanoTime();
        //taskInfos.values().stream().map(task -> task.stopped(ts, true)).filter(b->b).forEach(super);
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

        if (taskInfo.stopped(ts, false))
        {
            taskInfo.logTiming(ts);
        }
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

    @Override
    public Duration getTotalTime()
    {
        return Duration.ofNanos(taskInfos.values().stream().map(TaskInfo::getTotal).map(Duration::toNanos).reduce(0L, Long::sum));
    }
}
