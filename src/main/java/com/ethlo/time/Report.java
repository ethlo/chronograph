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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Report
{
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

        final List<TableRow> rows = new LinkedList<>();

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
            final TableRow row = getTableRow(chronograph, outputConfig, pf, nf, task);
            rows.add(row);
        }

        if (chronograph.getTaskNames().size() > 0)
        {
            rows.add(totals(chronograph));
        }

        return toString(getHeaderRow(outputConfig), rows);
    }

    private static String toString(TableRow header, final List<TableRow> rows)
    {
        final List<TableRow> all = new LinkedList<>();
        all.add(header);
        all.addAll(rows);
        final Map<Integer, Integer> maxLengths = getMaxLengths(all);
        final int totalWidth = getTotalWidth(maxLengths);
        final StringBuilder sb = new StringBuilder();

        sb.append("\n").append(repeat("-", totalWidth)).append("\n");
        sb.append(toString(header, maxLengths, false));
        sb.append("\n").append(repeat("-", totalWidth));

        for (TableRow row : rows)
        {
            sb.append("\n").append(toString(row, maxLengths, true));
        }

        return sb.toString();
    }

    private static int getTotalWidth(final Map<Integer, Integer> maxLengths)
    {
        final int padding = maxLengths.size() * 2;
        final int bars = maxLengths.size() + 1;
        final int cellData = maxLengths.values().stream().reduce(0, Integer::sum);
        return cellData + padding + bars;
    }

    private static String toString(final TableRow row, final Map<Integer, Integer> maxLengths, boolean left)
    {
        final StringBuilder sb = new StringBuilder();

        if (row.getCells().size() == 1)
        {
            // Totals row
            final int totalWidth = getTotalWidth(maxLengths);
            sb.append(repeat("-", totalWidth));
            sb.append("\n| ").append(adjustPadRight(row.getCells().get(0), totalWidth - 3)).append("|");
            sb.append("\n").append(repeat("-", totalWidth));
            return sb.toString();
        }
        else
        {
            // Normal row
            for (int i = 0; i < row.getCells().size(); i++)
            {
                final String value = row.getCells().get(i);
                final int width = maxLengths.get(i);
                final String cellValue = "| " + (left ? adjustPadLeft(value, width) : adjustPadRight(value, width)) + " ";
                sb.append(cellValue);
            }
            return sb.append("|").toString();
        }
    }

    private static Map<Integer, Integer> getMaxLengths(final List<TableRow> rows)
    {
        final Map<Integer, Integer> maxLengths = new HashMap<>();
        for (TableRow row : rows)
        {
            for (int i = 0; i < row.getCells().size(); i++)
            {
                maxLengths.compute(i, (key, value) ->
                {
                    final int cellLength = row.getCells().get(key).length();
                    return value != null ? Math.max(value, cellLength) : cellLength;
                });
            }
        }
        return maxLengths;
    }

    private static TableRow getTableRow(final Chronograph chronograph, final OutputConfig outputConfig, final NumberFormat pf, final NumberFormat nf, final TaskInfo task)
    {
        final TableRow row = new TableRow();

        row.append(task.getName());

        if (outputConfig.average())
        {
            final String avgTaskTimeStr = DurationUtil.humanReadable(task.getAverage());
            row.append(avgTaskTimeStr);
        }

        if (outputConfig.min())
        {
            final String minStr = DurationUtil.humanReadable(Duration.ofNanos(task.getMin()));
            row.append(minStr);
        }

        if (outputConfig.max())
        {
            final String maxStr = DurationUtil.humanReadable(Duration.ofNanos(task.getMax()));
            row.append(maxStr);
        }

        if (outputConfig.median())
        {
            final String medianStr = DurationUtil.humanReadable(task.getMedian());
            row.append(medianStr);
        }

        if (outputConfig.standardDeviation())
        {
            final String deviationStr = DurationUtil.humanReadable(task.getStandardDeviation());
            row.append(deviationStr);
        }

        if (outputConfig.percentiles() != null)
        {
            for (double percentile : outputConfig.percentiles())
            {
                final String percentileStr = DurationUtil.humanReadable(task.getPercentile(percentile));
                row.append(percentileStr);
            }
        }

        if (outputConfig.total())
        {
            final String totalTaskTimeStr = DurationUtil.humanReadable(Duration.ofNanos(task.getTotal()));
            row.append(totalTaskTimeStr);
        }

        if (outputConfig.invocations())
        {
            final String invocationsStr = nf.format(task.getInvocations());
            row.append(invocationsStr);
        }

        if (outputConfig.percentage())
        {
            final Duration totalTime = chronograph.getTotalTime();
            final double pct = totalTime.isZero() ? 0D : task.getTotal() / (double) totalTime.toNanos();
            row.append(pf.format(pct));
        }
        return row;
    }

    private static TableRow getHeaderRow(final OutputConfig outputConfig)
    {
        final TableRow headerRow = new TableRow();

        headerRow.append("Task");

        if (outputConfig.average())
        {
            headerRow.append("Average");
        }

        if (outputConfig.min())
        {
            headerRow.append("Min");
        }

        if (outputConfig.max())
        {
            headerRow.append("Max");
        }

        if (outputConfig.median())
        {
            headerRow.append("Median");
        }

        if (outputConfig.standardDeviation())
        {
            headerRow.append("Std dev");
        }

        if (outputConfig.percentiles() != null)
        {
            for (double percentile : outputConfig.percentiles())
            {
                headerRow.append(percentile + "th pctl");
            }
        }

        if (outputConfig.total())
        {
            headerRow.append("Total");
        }

        if (outputConfig.invocations())
        {
            headerRow.append("Count");
        }

        if (outputConfig.percentage())
        {
            headerRow.append("%");
        }
        return headerRow;
    }


    private static TableRow totals(final Chronograph chronograph)
    {
        return new TableRow().append(DurationUtil.humanReadable(chronograph.getTotalTime()));
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
        if (s.length() == width)
        {
            return s;
        }
        else if (s.length() > width)
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
