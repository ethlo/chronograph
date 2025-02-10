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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.ethlo.time.internal.MutableTaskInfo;
import com.ethlo.time.internal.util.LongList;
import com.ethlo.time.output.table.TableOutputFormatter;

class TableOutputformatterTest
{
    @Test
    void formatWithMultipleTasks()
    {
        // Prepare test data
        var stats1 = new LongList().addAll(List.of(454L, 566L, 499L, 504L, 604L));
        assertThat(stats1.sum()).isEqualTo(2627L);
        var stats2 = new LongList().addAll(List.of(1000_000L, 1200_000L, 1100_000L, 1150_000L));
        assertThat(stats2.sum()).isEqualTo(4_450_000L);
        var stats3 = new LongList().addAll(List.of(1200_100_000L, 1400_100_000L, 1800_100_000L));
        assertThat(stats3.sum()).isEqualTo(4_400_300_000L);

        var task1 = new MutableTaskInfo("Quick", null, stats1);
        var task2 = new MutableTaskInfo("Moderate", null, stats2);
        var task3 = new MutableTaskInfo("Slow", null, stats3);

        var chronographData = new ChronographData("My test", List.of(task1, task2, task3));
        assertThat(chronographData.getTotalTime().toNanos()).isEqualTo(4_404_752_627L);

        // Format the data
        var result = new TableOutputFormatter().format(chronographData);

        // Assert the result
        assertThat(result).isEqualToIgnoringWhitespace("""
                My test
                +----------+---------+-------+--------+---------+-----------+---------+---------+---------+
                | Task     | Timing   | Count | %      | Median  | Std dev  | Average | Min     | Max     |
                +----------+---------+-------+--------+---------+-----------+---------+---------+---------+
                | Quick    | 0.000 s |     5 |   0.0% |  504 ns |     53 ns |  525 ns |  454 ns |  604 ns |
                | Moderate | 0.004 s |     4 |   0.1% | 1.13 ms |  73.95 us | 1.11 ms | 1.00 ms | 1.20 ms |
                | Slow     | 4.400 s |     3 |  99.9% | 1.400 s | 249.44 ms | 1.467 s | 1.200 s | 1.800 s |
                +----------+---------+-------+--------+---------+-----------+---------+---------+---------+
                |      Sum | 4.405 s |    12 | 100.0% |         |           |         |         |         |
                +----------+---------+-------+--------+---------+-----------+---------+---------+---------+""");
    }
}
