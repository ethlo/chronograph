package com.ethlo.time;

/*-
 * #%L
 * chronograph
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

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Arrays;

public class Report
{
    /**
     * Generate a string with a table describing all tasks performed.
     * <p>For custom reporting, call {@link Chronograph#getTaskInfo()} and use the task info
     * directly.
     */
    public static String prettyPrint(Chronograph chronograph)
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("\n-------------------------------------------------------------------------------\n");
        sb.append("| Task            | Average        | Total           | Invocations   | %      |    \n");
        sb.append("-------------------------------------------------------------------------------\n");

        final NumberFormat pf = NumberFormat.getPercentInstance();
        pf.setMinimumFractionDigits(1);
        pf.setMaximumFractionDigits(1);
        pf.setMinimumIntegerDigits(2);
        pf.setGroupingUsed(false);

        final NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setGroupingUsed(true);

        for (String name : chronograph.getTaskNames())
        {
            final TaskInfo task = chronograph.getTaskInfo(name);

            final String totalTaskTimeStr = humanReadableFormat(Duration.ofNanos(task.getTotalTaskTime()));
            final String avgTaskTimeStr = humanReadableFormat(task.getAverageTaskTime());
            final String invocationsStr = nf.format(task.getInvocationCount());

            sb.append("| ");
            sb.append(adjustWidth(task.getName(), 15)).append(" | ");
            sb.append(adjustWidth(avgTaskTimeStr, 14)).append(" | ");
            sb.append(adjustWidth(totalTaskTimeStr, 15)).append(" | ");
            sb.append(adjustWidth(invocationsStr, 13)).append(" | ");
            final Duration totalTime = chronograph.getTotalTime();
            final double pct = totalTime.isZero() ? 0D : task.getTotalTaskTime() / (double) totalTime.toNanos();
            sb.append(adjustWidth(pf.format(pct), 6)).append(" |");
            sb.append("\n");
        }
        return sb.toString();
    }

    private static String humanReadableFormat(Duration duration)
    {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    private static String adjustWidth(final String s, final int width)
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
}
