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

    public static final double NANOS_PER_SECOND = 1_000_000_000D;

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
        if (taskPerformanceStats.size() > 1)
        {
            rows.add(SeparatorRow.getInstance());
            rows.add(totals(outputConfig, chronographData));
        }

        // Bottom
        rows.add(SeparatorRow.getInstance());

        return new Table(theme, rows).render(outputConfig.title() != null ? outputConfig.title() : chronographData.getName());
    }

    private static Comparator<? super TaskPerformanceStatistics> comparator(final OutputConfig outputConfig)
    {
        if (outputConfig.benchmarkMode())
        {
            return Comparator.comparingDouble((TaskPerformanceStatistics a) -> a.getThroughputStatistics().getAverage()).reversed();
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

        outputTotal(outputConfig, row, durationStatistics);

        addInvocations(outputConfig, taskStats, nf, row, invocations);

        outputPercentage(outputConfig, totalTime, pf, row, durationStatistics);

        conditionalOutput(outputConfig, row, multipleInvocations, outputConfig.median(), durationStatistics.getMedian(), throughputStatistics.getMedian());

        conditionalOutput(outputConfig, row, multipleInvocations, outputConfig.standardDeviation(), durationStatistics.getStandardDeviation(), throughputStatistics.getStandardDeviation());

        conditionalOutput(outputConfig, row, multipleInvocations, outputConfig.mean(), durationStatistics.getAverage(), throughputStatistics.getAverage());

        conditionalOutput(outputConfig, row, multipleInvocations, outputConfig.min(), durationStatistics.getMin(), throughputStatistics.getMin());

        conditionalOutput(outputConfig, row, multipleInvocations, outputConfig.max(), durationStatistics.getMax(), throughputStatistics.getMax());

        outputPercentiles(outputConfig, row, multipleInvocations, durationStatistics, throughputStatistics);

        return row;
    }

    private static void outputPercentiles(final OutputConfig outputConfig, final TableRow row, final boolean multipleInvocations, final PerformanceStatistics<Duration> durationStatistics, final PerformanceStatistics<Double> throughputStatistics)
    {
        if (outputConfig.percentiles() != null)
        {
            for (double percentile : outputConfig.percentiles())
            {
                outputCell(outputConfig, row, multipleInvocations, durationStatistics.getPercentile(percentile), throughputStatistics.getPercentile(percentile));
            }
        }
    }

    private static void outputTotal(final OutputConfig outputConfig, final TableRow row, final PerformanceStatistics<Duration> durationStatistics)
    {
        if (outputConfig.total())
        {
            final String str;
            if (outputConfig.formatting())
            {
                str = ReportUtil.humanReadable(durationStatistics.getElapsedTotal());
            }
            else
            {
                str = getRawNumberFormat().format(durationStatistics.getElapsedTotal().toNanos() / NANOS_PER_SECOND);
            }
            row.append(new TableCell(str, false, true));
        }
    }

    private static void outputPercentage(final OutputConfig outputConfig, final Duration totalTime, final NumberFormat pf, final TableRow row, final PerformanceStatistics<Duration> durationStatistics)
    {
        if (outputConfig.percentage())
        {
            final double pct = totalTime.isZero() ? 0D : durationStatistics.getElapsedTotal().toNanos() / (double) totalTime.toNanos();
            row.append(new TableCell(outputConfig.formatting() ? pf.format(pct) : getRawNumberFormat().format(pct), false, true));
        }
    }

    private static void addInvocations(final OutputConfig outputConfig, final TaskPerformanceStatistics taskStats, final NumberFormat nf, final TableRow row, final long invocations)
    {
        if (outputConfig.invocations())
        {
            String invocationsStr;
            if (taskStats.getSampleSize() != invocations && outputConfig.formatting())
            {
                // Reduced sample rate
                invocationsStr = "(" + nf.format(taskStats.getSampleSize()) + ") " + nf.format(invocations);
            }
            else
            {
                // Full sampling
                invocationsStr = outputConfig.formatting() ? nf.format(invocations) : getRawNumberFormat().format(invocations);
            }
            row.append(new TableCell(invocationsStr, false, true));
        }
    }

    private static void conditionalOutput(final OutputConfig outputConfig, final TableRow row, final boolean multipleInvocations, final boolean shouldShow, final Duration duration, final Double throughput)
    {
        if (shouldShow)
        {
            outputCell(outputConfig, row, multipleInvocations, duration, throughput);
        }
    }

    private static void outputCell(final OutputConfig outputConfig, final TableRow row, final boolean multipleInvocations, final Duration duration, final Double throughput)
    {
        if (multipleInvocations)
        {
            final String str;
            if (outputConfig.formatting())
            {
                str = outputConfig.getMode() == Mode.DURATION ? ReportUtil.humanReadable(duration) : ReportUtil.humanReadable(throughput);
            }
            else
            {
                final NumberFormat nf = getRawNumberFormat();
                str = outputConfig.getMode() == Mode.DURATION ? Long.toString(duration.toNanos()) : nf.format(throughput);
            }
            row.append(new TableCell(str, false, true));
        }
        else
        {
            row.append(new TableCell(""));
        }
    }

    private static NumberFormat getRawNumberFormat()
    {
        final NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setMinimumFractionDigits(6);
        nf.setMaximumFractionDigits(6);
        nf.setGroupingUsed(false);
        return nf;
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

    private static TableRow totals(final OutputConfig outputConfig, final ChronographData chronographData)
    {
        final long totalInvocations = chronographData.getTaskStatistics().stream().map(t -> t.getDurationStatistics().getTotalInvocations()).reduce(0L, Long::sum);
        return new TableRow()
                .append(new TableCell("Sum", false, false))
                .append(new TableCell(
                        outputConfig.formatting() ? ReportUtil.humanReadable(chronographData.getTotalTime()) : getRawNumberFormat().format(chronographData.getTotalTime().toNanos() / NANOS_PER_SECOND), false, true))
                .append(new TableCell(
                        outputConfig.formatting() ? ReportUtil.formatInteger(totalInvocations) : getRawNumberFormat().format(totalInvocations), false, true))
                .append(new TableCell(outputConfig.formatting() ? "100.0%" : "1.000000", false, true));
    }
}
