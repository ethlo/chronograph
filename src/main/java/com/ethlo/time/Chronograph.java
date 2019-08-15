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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Chronograph
{
    private final Map<String, TaskInfo> taskTimings;

    private Chronograph()
    {
        taskTimings = new ConcurrentHashMap<>(16);
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
        final TaskInfo taskTiming = taskTimings.computeIfAbsent(task, TaskInfo::new);
        taskTiming.started();
    }

    public void stop(String task)
    {
        final TaskInfo taskTiming = taskTimings.computeIfAbsent(task, TaskInfo::new);
        taskTiming.stopped();
    }

    public void resetAll()
    {
        taskTimings.clear();
    }

    public Duration getElapsedTime(final String task)
    {
        return Duration.of(getTaskInfo(task).getTotalTaskTime(), ChronoUnit.NANOS);
    }

    public TaskInfo getTaskInfo(final String task)
    {
        return Optional.ofNullable(taskTimings.get(task)).orElseThrow(() -> new IllegalStateException("Unknown task " + task));
    }

    public Set<String> getTaskNames()
    {
        return Collections.unmodifiableSet(taskTimings.keySet());
    }

    public List<TaskInfo> getTaskInfo()
    {
        return Collections.unmodifiableList(new ArrayList<>(taskTimings.values()));
    }

    /**
     * Generate a string with a table describing all tasks performed.
     * <p>For custom reporting, call {@link #getTaskInfo()} and use the task info
     * directly.
     */
    public String prettyPrint()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("\n-------------------------------------------------------------------------------\n");
        sb.append("| Task            | Average          | Total            | Invocations  | %    |    \n");
        sb.append("-------------------------------------------------------------------------------\n");

        final NumberFormat pf = NumberFormat.getPercentInstance();
        pf.setMinimumIntegerDigits(3);
        pf.setGroupingUsed(false);

        final NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setRoundingMode(RoundingMode.HALF_UP);
        //;MinimumIntegerDigits(9);
        nf.setGroupingUsed(true);

        for (TaskInfo task : taskTimings.values())
        {
            final String totalTaskTimeStr = humanReadableFormat(Duration.ofNanos(task.getTotalTaskTime()));
            final String avgTaskTimeStr = humanReadableFormat(task.getAverageTaskTime());
            final String invocationsStr = nf.format(task.getInvocationCount());

            sb.append("| ");
            sb.append(adjust(task.getName(), 15)).append(" | ");
            sb.append(adjust(totalTaskTimeStr, 16)).append(" | ");
            sb.append(adjust(avgTaskTimeStr, 16)).append(" | ");
            sb.append(adjust(invocationsStr, 12)).append(" | ");
            final double pct = task.getTotalTaskTime() * 100 / getTotalTime().toNanos();
            sb.append(adjust(Integer.toString((int) pct), 3)).append("% |");
            sb.append("\n");
        }
        return sb.toString();
    }

    private String humanReadableFormat(Duration duration) {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    private String adjust(final String s, final int width)
    {
        if (s.length() >= width)
        {
            return s.substring(0, width);
        }

        final char[] result = Arrays.copyOf(s.toCharArray(), width);
        for (int i = s.length(); i < width; i++)
        {
            result[i] = ' ';
        }
        return new String(result);
    }

    private Duration getTotalTime()
    {
        return Duration.of(taskTimings.values().stream().map(TaskInfo::getTotalTaskTime).reduce(0L, Long::sum), ChronoUnit.NANOS);
    }
}
