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
import java.util.LinkedList;
import java.util.List;

import com.ethlo.ascii.Table;
import com.ethlo.ascii.TableCell;
import com.ethlo.ascii.TableRow;

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

        return new Table(getHeaderRow(outputConfig), rows).render();
    }

    private static TableRow getTableRow(final Chronograph chronograph, final OutputConfig outputConfig, final NumberFormat pf, final NumberFormat nf, final TaskInfo task)
    {
        final TableRow row = new TableRow();

        row.append(new TableCell(task.getName()));

        if (outputConfig.average())
        {
            final String avgTaskTimeStr = DurationUtil.humanReadable(task.getAverage());
            row.append(new TableCell(avgTaskTimeStr, false));
        }

        if (outputConfig.min())
        {
            final String minStr = DurationUtil.humanReadable(task.getMin());
            row.append(new TableCell(minStr, false));
        }

        if (outputConfig.max())
        {
            final String maxStr = DurationUtil.humanReadable(task.getMax());
            row.append(new TableCell(maxStr, false));
        }

        if (outputConfig.median())
        {
            final String medianStr = DurationUtil.humanReadable(task.getMedian());
            row.append(new TableCell(medianStr, false));
        }

        if (outputConfig.standardDeviation())
        {
            final String deviationStr = DurationUtil.humanReadable(task.getStandardDeviation());
            row.append(new TableCell(deviationStr, false));
        }

        if (outputConfig.percentiles() != null)
        {
            for (double percentile : outputConfig.percentiles())
            {
                final String percentileStr = DurationUtil.humanReadable(task.getPercentile(percentile));
                row.append(new TableCell(percentileStr, false));
            }
        }

        if (outputConfig.total())
        {
            final String totalTaskTimeStr = DurationUtil.humanReadable(task.getTotal());
            row.append(new TableCell(totalTaskTimeStr, false));
        }

        if (outputConfig.invocations())
        {
            final String invocationsStr = nf.format(task.getInvocations());
            row.append(new TableCell(invocationsStr, false));
        }

        if (outputConfig.percentage())
        {
            final Duration totalTime = chronograph.getTotalTime();
            final double pct = totalTime.isZero() ? 0D : task.getTotal().toNanos() / (double) totalTime.toNanos();
            row.append(new TableCell(pf.format(pct), false));
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
}
