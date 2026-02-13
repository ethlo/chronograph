package com.ethlo.chronograph;

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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.ethlo.chronograph.internal.ascii.ReportUtil;
import com.ethlo.chronograph.output.table.TableOutputFormatter;
import com.ethlo.chronograph.output.table.TableThemes;
import com.ethlo.util.BaseTest;
import com.ethlo.util.SleepUtil;

public class ChronographTest extends BaseTest
{
    private final String taskName = "a long task name";

    @Test
    void startStopSequenceSingleTask()
    {
        final Chronograph chronograph = Chronograph.create();

        for (int i = 1; i <= 10; i++)
        {
            chronograph.start(taskName);
            millisecondTask();
            chronograph.stop();
        }

        output(chronograph);
        assertThat(true).isTrue();
    }

    @Test
    void hierarchicalTest()
    {
        final Chronograph c = Chronograph.create();

        c.time("Request", () ->
        {
            for (int i = 0; i < 13; i++)
            {
                c.time("Fetch", () -> busy(12));
            }

            // Overhead
            busy(300);

            c.time("De-serialize", () ->
            {
                // Overhead
                busy(14);
                c.time("JSON de-serialize", () -> busy(44));
            });
        });

        c.time("Response", () ->
        {
            c.time("Logging", () -> busy(11));

            c.time("Serialize", () ->
            {
                // Overhead
                busy(14);
                c.time("JSON serialize", () -> busy(27));
            });
        });

        output(c, OutputConfig.COMPACT.overheadName("<overhead>")
                .overheadThreshold(0.1)
                .percentage(true)
                .standardDeviation(false)
                .median(true)
                .max(true), TableThemes.OCEAN_BREEZE);

        output(c, TableThemes.ASCII);
        output(c, TableThemes.OCEAN_BREEZE);
        output(c, TableThemes.MIDNIGHT_GOLD);
        output(c, TableThemes.SILVER_STEEL);
        output(c, TableThemes.RED_HERRING);
        output(c, TableThemes.GRAPHITE_EMBER);
        output(c, TableThemes.ROYAL_INDIGO);
        output(c, TableThemes.SINGLE);
        output(c, TableThemes.DOUBLE);
        output(c, TableThemes.ROUNDED);
        output(c, TableThemes.MINIMAL);
        output(c, TableThemes.COMPACT);
        output(c);

        final List<String> flatList = c.getTaskData().getTasks().stream().map(TaskInfo::getName).toList();
        assertThat(flatList).containsExactly("Request", "Fetch", "De-serialize",
                "JSON de-serialize", "<overhead>", "<overhead>", "Response", "Logging",
                "Serialize", "JSON serialize", "<overhead>"
        );
    }

    private void busy(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().notify();
        }
    }

    @Test
    void sequenceMultipleFunctionTasks()
    {
        final Chronograph chronograph = Chronograph.create();

        for (int i = 1; i <= 100_000; i++)
        {
            assertThat(chronograph.time("foo", this::microsecondTaskInput, 1)).isEqualTo(2);
        }
    }

    @Test
    void sequenceMultipleRunnableTasks()
    {
        final Chronograph chronograph = Chronograph.create();

        for (int i = 1; i <= 100_000; i++)
        {
            chronograph.time("foo", this::microsecondTask);
            final int input = i;
            chronograph.time("bar", () -> microsecondTaskInput(input));
            chronograph.time("baz baz baz baz baz baz", this::microsecondTask);
        }

        assertThat(chronograph.getTasks()).hasSize(3);
        output(chronograph);
    }

    private void millisecondTask()
    {
        try
        {
            SleepUtil.sleepNanos(1_000_000);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().notify();
        }
    }

    private void microsecondTask()
    {
        try
        {
            SleepUtil.sleepNanos(1_000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private int microsecondTaskInput(int input)
    {
        try
        {
            SleepUtil.sleepNanos(1_000);
            return input + input;
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    void stopBeforeStart()
    {
        final Chronograph chronograph = Chronograph.create();
        assertThat(chronograph.stop()).isFalse(); // Does nothing, we are not running
    }

    @Test
    void stopAfterStopped()
    {
        final Chronograph chronograph = Chronograph.create();
        chronograph.start(taskName);
        assertThat(chronograph.stop()).isTrue();
        assertThat(chronograph.stop()).isFalse(); // Does nothing, already stopped
    }

    @Test
    void nullTask()
    {
        final Chronograph chronograph = Chronograph.create();
        Assertions.assertThrows(IllegalArgumentException.class, () -> chronograph.start(null));
    }

    @Test
    void noData()
    {
        assertThat(new TableOutputFormatter().format(Chronograph.create().getTaskData())).isEqualTo("No performance data");
    }

    @Test
    void sequentialStart()
    {
        final Chronograph chronograph = Chronograph.create();
        assertThat(chronograph.start(taskName)).isTrue();
        assertThat(chronograph.start(taskName)).isFalse(); // Does nothing, already running
    }

    @Test
    void testSimpleSingleInvocations()
    {
        final Chronograph chronograph = Chronograph.create();
        for (int i = 0; i < 10; i++)
        {
            chronograph.time("Task-" + i, () -> {
            });
        }
        output(chronograph);
        assertThat(true).isTrue();
    }

    @Test
    void testGranularity()
    {
        final Chronograph chronograph = Chronograph.create();
        for (int i = 0; i < 1_000_000; i++)
        {
            chronograph.start(taskName);
            chronograph.stop();
        }
        final Duration median = chronograph.getTask(taskName).getStatistics().getMedian();
        logger.info("Granularity: {}", ReportUtil.humanReadable(median));
        assertThat(median.toNanos()).isGreaterThan(0);
    }

    @Test
    void resetAll()
    {
        final Chronograph chronograph = Chronograph.create();
        chronograph.start(taskName);
        chronograph.stop();
        assertThat(chronograph.getTasks().stream().map(TaskInfo::getName)).containsExactly(taskName);
        chronograph.resetAll();
        assertThat(chronograph.getTasks()).isEmpty();
    }

    @Test
    void testIsRunning()
    {
        final Chronograph chronograph = Chronograph.create();
        assertThat(chronograph.isRunning(taskName)).isFalse();
        chronograph.start(taskName);
        assertThat(chronograph.isRunning(taskName)).isTrue();
        chronograph.stop();
        assertThat(chronograph.isRunning(taskName)).isFalse();
    }

    @Test
    void getTaskInfo()
    {
        final Chronograph chronograph = Chronograph.create();
        chronograph.start("a");
        chronograph.start("b");
        chronograph.start("c");
        chronograph.stopAll();
        assertThat(chronograph.getTasks()).hasSize(1);
    }

    @Test
    void getTotalTaskTimeForEmpty()
    {
        final Chronograph chronograph = Chronograph.create();
        assertThat(chronograph.getTotalTime()).isEqualTo(Duration.ZERO);
        chronograph.start(taskName);
        assertThat(chronograph.getTotalTime()).isEqualTo(Duration.ZERO);
        chronograph.stop();
        assertThat(chronograph.getTotalTime()).isNotEqualTo(Duration.ZERO);
    }

    @Test
    void testIsAnyRunning()
    {
        final Chronograph chronograph = Chronograph.create();
        chronograph.start(taskName);
        assertThat(chronograph.isAnyRunning()).isTrue();
        chronograph.stop();
        assertThat(chronograph.isAnyRunning()).isFalse();
    }

    @Test
    void testGetAverageForNonStoppedTask()
    {
        final Chronograph chronograph = Chronograph.create();
        chronograph.start(taskName);
        assertThat(chronograph.getTask(taskName).getStatistics().getAverage()).isEqualTo(Duration.ZERO);
    }

    @Test
    void testMergeResults()
    {
        final Chronograph chronograph1 = Chronograph.create();
        chronograph1.start(taskName);

        final Chronograph chronograph2 = Chronograph.create();
        chronograph2.start(taskName);

        chronograph1.stop();
        chronograph2.stop();

        output(chronograph1, TableThemes.DOUBLE);
        output(chronograph2, TableThemes.COMPACT);
        final ChronographData merged = chronograph2.getTaskData().merge("merged", chronograph1.getTaskData());

        System.out.println(new TableOutputFormatter().format(merged));

        assertThat(true).isTrue();
    }

    @Test
    void recursiveStartCompatibility()
    {
        final Chronograph chronograph = Chronograph.create();
        final String task = "recursive-task";

        // Level 1
        assertThat(chronograph.start(task)).isTrue();

        // Level 2 (Same name)
        assertThat(chronograph.start(task)).as("Should not allow re-entry of same name").isFalse();

        // Level 3 (Still same name)
        assertThat(chronograph.start(task)).isFalse();

        // Verify stack integrity
        assertThat(chronograph.isAnyRunning()).isTrue();

        // Stop Level 1
        assertThat(chronograph.stop()).isTrue();

        // Everything should be closed now because levels 2 and 3 never "started"
        assertThat(chronograph.isAnyRunning()).isFalse();
        assertThat(chronograph.stop()).as("Extra stop should fail").isFalse();
    }

    @Test
    void siblingSameName()
    {
        final Chronograph chronograph = Chronograph.create();

        // Start A -> Sub
        chronograph.start("A");
        assertThat(chronograph.start("Sub")).isTrue();
        chronograph.stop(); // Stop Sub
        chronograph.stop(); // Stop A

        // Start B -> Sub
        chronograph.start("B");
        // This should be TRUE because "Sub" is not currently in the active stack
        assertThat(chronograph.start("Sub")).isTrue();
        chronograph.stop();
        chronograph.stop();

        // The data should be separate now
        assertThat(chronograph.getTaskData().getRootTasks()).hasSize(2); // A and B
        assertThat(chronograph.getTaskData().getTasks()).hasSize(4); // All 4
    }
}
