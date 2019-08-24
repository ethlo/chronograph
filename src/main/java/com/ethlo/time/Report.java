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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.ethlo.ascii.SeparatorRow;
import com.ethlo.ascii.Table;
import com.ethlo.ascii.TableCell;
import com.ethlo.ascii.TableRow;
import com.ethlo.ascii.TableTheme;
import com.ethlo.time.statistics.PerformanceStatistics;

public class Report
{
    public static String prettyPrint(ChronographData chronographData, OutputConfig outputConfig, TableTheme theme)
    {
        if (chronographData.isEmpty())
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

        // Header rows
        rows.add(SeparatorRow.getInstance());
        rows.add(getHeaderRow(outputConfig));
        rows.add(SeparatorRow.getInstance());

        // Task data
        final List<TaskPerformanceStatistics> taskPerformanceStats = new ArrayList<>(chronographData.getTaskStatistics());
        taskPerformanceStats.sort(comparator(outputConfig));
        for (TaskPerformanceStatistics taskStats : taskPerformanceStats)
        {
            rows.add(getTableRow(outputConfig, chronographData.getTotalTime(), taskStats, pf, nf));
        }

        // Summary row
        rows.add(SeparatorRow.getInstance());
        rows.add(totals(chronographData));
        rows.add(SeparatorRow.getInstance());

        return new Table(theme, rows).render(outputConfig.title());
    }

    private static Comparator<? super TaskPerformanceStatistics> comparator(final OutputConfig outputConfig)
    {
        if (outputConfig.benchmarkMode())
        {
            return Comparator.comparing(a -> a.getThroughputStatistics().getElapsedTotal());
        }
        return (a, b) -> 0;
    }

    private static TableRow getTableRow(final OutputConfig outputConfig, Duration totalTime, TaskPerformanceStatistics taskStats, final NumberFormat pf, final NumberFormat nf)
    {
        final TableRow row = new TableRow();

        row.append(new TableCell(taskStats.getName()));

        final long invocations = taskStats.getThroughputStatistics().getTotalInvocations();
        final boolean multipleInvocations = invocations > 1;

        final PerformanceStatistics<Duration> durationStatistics = taskStats.getDurationStatistics();
        final PerformanceStatistics<Double> throughputStatistics = taskStats.getThroughputStatistics();

        if (outputConfig.total())
        {
            final String totalTaskTimeStr = ReportUtil.humanReadable(durationStatistics.getElapsedTotal());
            row.append(new TableCell(totalTaskTimeStr, false, true));
        }

        if (outputConfig.invocations())
        {
            String invocationsStr;
            if (taskStats.getSampleSize() != invocations)
            {
                // Reduced sample rate
                invocationsStr = "(" + nf.format(taskStats.getSampleSize()) + ") " + nf.format(invocations);
            }
            else
            {
                // Full sampling
                invocationsStr = nf.format(invocations);
            }
            row.append(new TableCell(invocationsStr, false, true));
        }

        if (outputConfig.percentage())
        {
            final double pct = totalTime.isZero() ? 0D : durationStatistics.getElapsedTotal().toNanos() / (double) totalTime.toNanos();
            row.append(new TableCell(pf.format(pct), false, true));
        }

        if (outputConfig.median())
        {
            outputCell(outputConfig, row, multipleInvocations, durationStatistics.getMedian(), throughputStatistics.getMedian());
        }

        if (outputConfig.standardDeviation())
        {
            outputCell(outputConfig, row, multipleInvocations, durationStatistics.getStandardDeviation(), throughputStatistics.getStandardDeviation());
        }

        if (outputConfig.mean())
        {
            outputCell(outputConfig, row, multipleInvocations, durationStatistics.getAverage(), throughputStatistics.getAverage());
        }

        if (outputConfig.min())
        {
            outputCell(outputConfig, row, multipleInvocations, durationStatistics.getMin(), throughputStatistics.getMin());
        }

        if (outputConfig.max())
        {
            outputCell(outputConfig, row, multipleInvocations, durationStatistics.getMax(), throughputStatistics.getMax());
        }

        if (outputConfig.percentiles() != null)
        {
            for (double percentile : outputConfig.percentiles())
            {
                outputCell(outputConfig, row, multipleInvocations, durationStatistics.getPercentile(percentile), throughputStatistics.getPercentile(percentile));
            }
        }

        return row;
    }

    private static void outputCell(final OutputConfig outputConfig, final TableRow row, final boolean multipleInvocations, final Duration duration, final Double throughput)
    {
        if (multipleInvocations)
        {
            final String str = outputConfig.getMode() == Mode.DURATION ? ReportUtil.humanReadable(duration) : ReportUtil.humanReadable(throughput);
            row.append(new TableCell(str, false, true));
        }
        row.append(new TableCell(""));
    }

    private static TableRow getHeaderRow(final OutputConfig outputConfig)
    {
        final TableRow headerRow = new TableRow();

        headerRow.append("Task");

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

        if (outputConfig.median())
        {
            headerRow.append("Median");
        }

        if (outputConfig.standardDeviation())
        {
            headerRow.append("Std dev");
        }

        if (outputConfig.mean())
        {
            headerRow.append("Mean");
        }

        if (outputConfig.min())
        {
            headerRow.append("Min");
        }

        if (outputConfig.max())
        {
            headerRow.append("Max");
        }

        if (outputConfig.percentiles() != null)
        {
            final NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMinimumFractionDigits(0);

            for (double percentile : outputConfig.percentiles())
            {
                headerRow.append(nf.format(percentile) + " pctl");
            }
        }

        return headerRow;
    }

    private static TableRow totals(final ChronographData chronographData)
    {
        return new TableRow()
                .append(new TableCell("Sum", false, false))
                .append(new TableCell(ReportUtil.humanReadable(chronographData.getTotalTime()), false, true))
                .append(new TableCell(ReportUtil.formatInteger(chronographData.getTaskStatistics().stream().map(t -> t.getDurationStatistics().getTotalInvocations()).reduce(0L, Long::sum)), false, true))
                .append(new TableCell("100%", false, true));
    }
}
