package com.ethlo.time;

/*-
 * #%L
 * Chronograph
 * %%
 * Copyright (C) 2019 - 2024 Morten Haraldsen (ethlo)
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

import static com.ethlo.ascii.Table.EMPTY_CONTENT;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.ethlo.ascii.SeparatorRow;
import com.ethlo.ascii.Table;
import com.ethlo.ascii.TableCell;
import com.ethlo.ascii.TableRow;
import com.ethlo.ascii.TableTheme;
import com.ethlo.ascii.TableThemes;
import com.ethlo.time.statistics.PerformanceStatistics;

public class TableOutputformatter implements OutputFormatter
{
    private final TableTheme tableTheme;
    private final OutputConfig outputConfig;

    public TableOutputformatter(TableTheme tableTheme, OutputConfig outputConfig)
    {
        this.tableTheme = Objects.requireNonNull(tableTheme, "tableTheme cannot be null");
        this.outputConfig = Objects.requireNonNull(outputConfig, "outputConfig cannot be null");
    }

    public TableOutputformatter()
    {
        this(TableThemes.ASCII, OutputConfig.DEFAULT);
    }

    private static TableRow getTableRow(final OutputConfig outputConfig, Duration totalTime, TaskInfo taskInfo, final NumberFormat pf, final NumberFormat nf)
    {
        final TableRow row = new TableRow();

        row.append(new TableCell("  ".repeat(taskInfo.depth()) + taskInfo.getName()));

        final long invocations = taskInfo.getPerformanceStatistics().getTotalInvocations();
        final boolean multipleInvocations = invocations > 1;
        final PerformanceStatistics performanceStatistics = taskInfo.getPerformanceStatistics();

        outputTotal(outputConfig, row, taskInfo, totalTime);

        addInvocations(outputConfig, taskInfo, nf, row, invocations);
        outputPercentage(outputConfig, totalTime, pf, row, taskInfo);

        conditionalOutput(row, multipleInvocations, outputConfig.median(), performanceStatistics.getMedian());
        conditionalOutput(row, multipleInvocations, outputConfig.standardDeviation(), performanceStatistics.getStandardDeviation());
        conditionalOutput(row, multipleInvocations, outputConfig.mean(), performanceStatistics.getAverage());
        conditionalOutput(row, multipleInvocations, outputConfig.min(), performanceStatistics.getMin());
        conditionalOutput(row, multipleInvocations, outputConfig.max(), performanceStatistics.getMax());
        outputPercentiles(outputConfig, row, multipleInvocations, performanceStatistics);

        return row;
    }

    private static void outputPercentiles(final OutputConfig outputConfig, final TableRow row, final boolean multipleInvocations, final PerformanceStatistics performanceStatistics)
    {
        if (outputConfig.percentiles() != null)
        {
            for (double percentile : outputConfig.percentiles())
            {
                outputCell(row, multipleInvocations, performanceStatistics.getPercentile(percentile));
            }
        }
    }

    private static void outputTotal(final OutputConfig outputConfig, final TableRow row, final TaskInfo taskInfo, final Duration totalTime)
    {
        if (outputConfig.total())
        {
            final ChronoUnit summaryResolution = ReportUtil.getSummaryResolution(totalTime);
            final String str = ReportUtil.humanReadable(taskInfo.getPerformanceStatistics().getElapsedTotal(), summaryResolution);
            row.append(new TableCell("  ".repeat(taskInfo.depth()) + str, true, true));
        }
    }

    private static void outputPercentage(final OutputConfig outputConfig, final Duration totalTime, final NumberFormat pf, final TableRow row, final TaskInfo taskInfo)
    {
        if (outputConfig.percentage())
        {
            final double pct = totalTime.isZero() ? 0D : taskInfo.getPerformanceStatistics().getElapsedTotal().toNanos() / (double) (taskInfo.getParent() != null ? taskInfo.getParent().getTotalTaskTime().toNanos() : totalTime.toNanos());
            row.append(new TableCell("  ".repeat(taskInfo.depth()) + pf.format(pct), true, true));
        }
    }

    private static void addInvocations(final OutputConfig outputConfig, final TaskInfo taskInfo, final NumberFormat nf, final TableRow row, final long invocations)
    {
        if (outputConfig.invocations())
        {
            String invocationsStr;
            if (taskInfo.getSampleSize() != invocations)
            {
                // Reduced sample rate
                invocationsStr = "(" + nf.format(taskInfo.getSampleSize()) + ") " + nf.format(invocations);
            }
            else
            {
                // Full sampling
                invocationsStr = nf.format(invocations);
            }
            row.append(new TableCell(invocationsStr, false, true));
        }
    }

    private static void conditionalOutput(final TableRow row, final boolean multipleInvocations, final boolean shouldShow, final Duration duration)
    {
        if (shouldShow)
        {
            outputCell(row, multipleInvocations, duration);
        }
    }

    private static void outputCell(final TableRow row, final boolean multipleInvocations, final Duration duration)
    {
        if (multipleInvocations)
        {
            final String str = ReportUtil.humanReadable(duration);
            row.append(new TableCell(str, false, true));
        }
        else
        {
            row.append(new TableCell(EMPTY_CONTENT));
        }
    }

    private static TableRow getHeaderRow(final OutputConfig outputConfig)
    {
        final TableRow headerRow = new TableRow();
        headerRow.append("Task");

        if (outputConfig.total())
        {
            headerRow.append("Timing");
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

    private TableRow totals(final ChronographData chronographData)
    {
        final long totalInvocations = chronographData.getTasks().stream().map(TaskInfo::getTotalTaskInvocations).reduce(0L, Long::sum);
        final TableRow tableRow = new TableRow()
                .append(new TableCell("Sum", false, false));

        tableRow.append(new TableCell(ReportUtil.humanReadable(chronographData.getTotalTime()), false, true));

        tableRow.append(new TableCell(ReportUtil.formatInteger(totalInvocations), false, true));

        if (outputConfig.percentage())
        {
            tableRow.append(new TableCell("100.0%", false, true));
        }

        return tableRow;
    }

    @Override
    public String format(final ChronographData chronographData)
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

        rows.add(SeparatorRow.getInstance());
        rows.add(getHeaderRow(outputConfig));
        rows.add(SeparatorRow.getInstance());

        doOutputTasks(chronographData.getTotalTime(), chronographData.getTotalTime(), chronographData.getRootTasks(), rows, pf, nf);

        if (chronographData.getRootTasks().size() > 1)
        {
            rows.add(SeparatorRow.getInstance());
            rows.add(totals(chronographData));
        }

        rows.add(SeparatorRow.getInstance());
        return new Table(tableTheme, rows).render(outputConfig.title() != null ? outputConfig.title() : chronographData.getName());
    }

    private void doOutputTasks(final Duration totalTime, Duration parentDuration, List<TaskInfo> children, List<TableRow> rows, NumberFormat pf, NumberFormat nf)
    {
        if (children.isEmpty())
        {
            return;
        }

        final List<TaskInfo> combined = new ArrayList<>(children);

        // Find all at this level
        final long allAtLevel = children.stream().mapToLong(c -> c.getTotalTaskTime().toNanos()).sum();
        final long diff = parentDuration.toNanos() - allAtLevel;
        if (diff / (double) parentDuration.toNanos() > 0.02)
        {
            final TaskInfo overheadTask = new TaskInfo(outputConfig.overheadName(), children.get(0).getParent());
            overheadTask.data.add(diff);
            combined.add(overheadTask);
        }

        for (TaskInfo taskInfo : combined)
        {
            rows.add(getTableRow(outputConfig, totalTime, taskInfo, pf, nf));

            // Recurse down the task hierarchy
            doOutputTasks(totalTime, taskInfo.getTotalTaskTime(), taskInfo.getChildren(), rows, pf, nf);
        }
    }
}
