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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

public class Chronograph
{
    private final ConcurrentLinkedQueue<String> order = new ConcurrentLinkedQueue<>();
    private final Map<String, TaskInfo> taskInfos;

    private Chronograph()
    {
        taskInfos = new ConcurrentHashMap<>(16);
    }

    public static Chronograph create()
    {
        return new Chronograph();
    }

    public void start(String task)
    {
        if (task == null)
        {
            throw new IllegalArgumentException("task cannot be null");
        }

        final TaskInfo taskTiming = taskInfos.computeIfAbsent(task, taskName -> {
            order.add(taskName);
            return new TaskInfo(taskName);
        });
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
        order.clear();
    }

    public Duration getElapsedTime(final String task)
    {
        return Duration.of(getTaskInfo(task).getTotalTaskTime(), ChronoUnit.NANOS);
    }

    public TaskInfo getTaskInfo(final String task)
    {
        return Optional.ofNullable(taskInfos.get(task)).orElseThrow(() -> new IllegalStateException("Unknown task " + task));
    }

    public Collection<String> getTaskNames()
    {
        return Collections.unmodifiableCollection(order);
    }

    public List<TaskInfo> getTaskInfo()
    {
        return Collections.unmodifiableList(new ArrayList<>(taskInfos.values()));
    }

    public boolean isRunning(String task)
    {
        final TaskInfo taskInfo = taskInfos.get(task);
        return taskInfo != null && taskInfo.isRunning();
    }

    /**
     * See {@link Report#prettyPrint(Chronograph)}
     *
     * @return A formatted string with the task details
     */
    public String prettyPrint()
    {
        return Report.prettyPrint(this);
    }

    public Duration getTotalTime()
    {
        return Duration.ofNanos(taskInfos.values().stream().map(TaskInfo::getTotalTaskTime).reduce(0L, Long::sum));
    }

    public void timed(final String taskName, final Runnable task)
    {
        try
        {
            start(taskName);
            task.run();
        }
        finally
        {
            stop(taskName);
        }
    }

    public <R,T> R timedFunction(final String taskName, final Function<T, R> task, T input)
    {
        try
        {
            start(taskName);
            return task.apply(input);
        }
        finally
        {
            stop(taskName);
        }
    }
}
