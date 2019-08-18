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
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_GRAY_BACKGROUND = "\u001B[47m";

    /**
     * Generate a string with a table describing all tasks performed.
     * <p>For custom reporting, call {@link Chronograph#getTaskInfo()} and use the task info
     * directly.
     */
    public static String prettyPrint(Chronograph chronograph, OutputConfig outputConfig)
    {
        if (chronograph.getTaskInfo().isEmpty())
        {
            return "No performance data";
        }

        final StringBuilder sb = new StringBuilder(outputConfig.title() != null ? outputConfig.title() : "");

        final StringBuilder header = new StringBuilder();
        header.append(adjustPadRight("| Task", 23));

        if (outputConfig.average())
        {
            header.append(adjustPadRight(" | Average", 15));
        }

        if (outputConfig.min())
        {
            header.append(adjustPadRight(" | Min", 15));
        }

        if (outputConfig.max())
        {
            header.append(adjustPadRight(" | Max", 15));
        }

        if (outputConfig.median())
        {
            header.append(adjustPadRight(" | Median", 15));
        }

        if (outputConfig.standardDeviation())
        {
            header.append(adjustPadRight(" | Std dev", 15));
        }

        if (outputConfig.percentiles() != null)
        {
            for (double percentile : outputConfig.percentiles())
            {
                header.append(adjustPadRight(" | " + percentile + "th pctl", 15));
            }
        }

        if (outputConfig.total())
        {
            header.append(adjustPadRight(" | Total", 18));
        }

        if (outputConfig.invocations())
        {
            header.append(adjustPadRight(" | Invocations", 15));
        }

        if (outputConfig.percentage())
        {
            header.append(adjustPadRight(" | %", 9));
        }

        header.append(" |");

        final int totalWidth = header.length();

        sb.append("\n").append(repeat("-", totalWidth)).append("\n");
        sb.append(header);
        sb.append("\n").append(repeat("-", totalWidth)).append("\n");

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

            sb.append("| ");
            sb.append(adjustPadRight(task.getName(), 21)).append(" | ");

            if (outputConfig.average())
            {
                final String avgTaskTimeStr = DurationUtil.humanReadable(task.getAverage());
                sb.append(adjustPadLeft(avgTaskTimeStr, 12)).append(" | ");
            }

            if (outputConfig.min())
            {
                final String minStr = DurationUtil.humanReadable(Duration.ofNanos(task.getMin()));
                sb.append(adjustPadLeft(minStr, 12)).append(" | ");
            }

            if (outputConfig.max())
            {
                final String maxStr = DurationUtil.humanReadable(Duration.ofNanos(task.getMax()));
                sb.append(adjustPadLeft(maxStr, 12)).append(" | ");
            }

            if (outputConfig.median())
            {
                final String medianStr = DurationUtil.humanReadable(Duration.ofNanos((long) task.getMedian()));
                sb.append(adjustPadLeft(medianStr, 12)).append(" | ");
            }

            if (outputConfig.standardDeviation())
            {
                final String deviationStr = DurationUtil.humanReadable(Duration.ofNanos((long) task.getStandardDeviation()));
                sb.append(adjustPadLeft(deviationStr, 12)).append(" | ");
            }

            if (outputConfig.percentiles() != null)
            {
                for (double percentile : outputConfig.percentiles())
                {
                    final String percentileStr = DurationUtil.humanReadable(Duration.ofNanos((long) task.getPercentile(percentile)));
                    sb.append(adjustPadLeft(percentileStr, 12)).append(" | ");
                }
            }

            if (outputConfig.total())
            {
                final String totalTaskTimeStr = DurationUtil.humanReadable(Duration.ofNanos(task.getTotal()));
                sb.append(adjustPadLeft(totalTaskTimeStr, 15)).append(" | ");
            }

            if (outputConfig.invocations())
            {
                final String invocationsStr = nf.format(task.getInvocations());
                sb.append(adjustPadLeft(invocationsStr, 12)).append(" | ");
            }

            if (outputConfig.percentage())
            {
                final Duration totalTime = chronograph.getTotalTime();
                final double pct = totalTime.isZero() ? 0D : task.getTotal() / (double) totalTime.toNanos();
                sb.append(adjustPadLeft(pf.format(pct), 6)).append(" |");
            }

            sb.append("\n");
        }

        if (chronograph.getTaskNames().size() > 1)
        {
            sb.append(totals(chronograph, totalWidth));
        }

        return sb.toString();
    }


    private static String totals(final Chronograph chronograph, final int totalWidth)
    {
        return repeat("-", totalWidth) + "\n" +
                "| " + adjustPadRight("Total" + ": " + DurationUtil.humanReadable(chronograph.getTotalTime()), totalWidth - 4) + " |" + "\n" +
                repeat("-", totalWidth) + "\n";
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
