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
        if (chronograph.getTaskInfo().isEmpty())
        {
            return "No performance data";
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("\n--------------------------------------------------------------------------------\n");
        sb.append("| Task                  | Average      | Total        | Invocations   | %      |    \n");
        sb.append("--------------------------------------------------------------------------------\n");

        final NumberFormat pf = NumberFormat.getPercentInstance();
        pf.setMinimumFractionDigits(1);
        pf.setMaximumFractionDigits(1);
        pf.setGroupingUsed(false);

        final NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setGroupingUsed(true);

        for (String name : chronograph.getTaskNames())
        {
            final TaskInfo task = chronograph.getTaskInfo(name);

            final String totalTaskTimeStr = DurationUtil.humanReadable(Duration.ofNanos(task.getTotal()));
            final String avgTaskTimeStr = DurationUtil.humanReadable(task.getAverage());
            final String invocationsStr = nf.format(task.getInvocations());

            sb.append("| ");
            sb.append(adjustPadRight(task.getName(), 21)).append(" | ");
            sb.append(adjustPadLeft(avgTaskTimeStr, 12)).append(" | ");
            sb.append(adjustPadLeft(totalTaskTimeStr, 12)).append(" | ");
            sb.append(adjustPadLeft(invocationsStr, 13)).append(" | ");
            final Duration totalTime = chronograph.getTotalTime();
            final double pct = totalTime.isZero() ? 0D : task.getTotal() / (double) totalTime.toNanos();
            sb.append(adjustPadLeft(pf.format(pct), 6)).append(" |");
            sb.append("\n");
        }

        if (chronograph.getTaskNames().size() > 1)
        {
            sb.append(totals(chronograph));
        }

        return sb.toString();
    }

    private static String totals(final Chronograph chronograph)
    {
        return repeat("-", 80) + "\n" +
                "| " + adjustPadRight("Total" + ": " + DurationUtil.humanReadable(chronograph.getTotalTime()), 76) + " |" + "\n" +
                repeat("-", 80) + "\n";
    }

    private static String repeat(final String s, final int count)
    {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++)
        {
            sb.append(s);
        }
        return sb.toString();
    }


    private static String adjustPadRight(final String s, final int width)
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

    private static String adjustPadLeft(final String s, final int width)
    {
        if (s.length() >= width)
        {
            return s.substring(0, width);
        }

        final char[] result = new char[width];
        Arrays.fill(result, ' ');
        for (int i = 0; i < s.length(); i++)
        {
            result[i + (width - s.length())] = s.charAt(i);
        }
        return new String(result);
    }
}
