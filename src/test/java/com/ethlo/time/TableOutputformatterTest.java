package com.ethlo.time;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.ethlo.time.statistics.PerformanceStatistics;
import com.ethlo.util.IndexedCollectionStatistics;
import com.ethlo.util.LongList;

class TableOutputformatterTest
{

    @Test
    void format()
    {
        final List<TaskPerformanceStatistics> taskData = List.of(
                new TaskPerformanceStatistics("Quick", 20, new PerformanceStatistics(new IndexedCollectionStatistics(new LongList().addAll(List.of(454L, 566L, 499L, 504L, 604L))))));
        final ChronographData chronographData = new ChronographData("My test", taskData);
        final String result = new TableOutputformatter().format(chronographData);
        //assertThat(result).isEqualTo("mehh");
    }

    @Test
    void formatWithMultipleTasks()
    {
        // Prepare test data
        var stats1 = new IndexedCollectionStatistics(new LongList().addAll(List.of(454L, 566L, 499L, 504L, 604L)));
        assertThat(stats1.getSum()).isEqualTo(2627L);
        var stats2 = new IndexedCollectionStatistics(new LongList().addAll(List.of(1000_000L, 1200_000L, 1100_000L, 1150_000L)));
        assertThat(stats2.getSum()).isEqualTo(4_450_000L);
        var stats3 = new IndexedCollectionStatistics(new LongList().addAll(List.of(1200_100_000L, 1400_100_000L, 1800_100_000L)));
        assertThat(stats3.getSum()).isEqualTo(4_400_300_000L);

        var task1 = new TaskPerformanceStatistics("Quick", new PerformanceStatistics(stats1));
        var task2 = new TaskPerformanceStatistics("Moderate", new PerformanceStatistics(stats2));
        var task3 = new TaskPerformanceStatistics("Slow", new PerformanceStatistics(stats3));

        var chronographData = new ChronographData("My test", List.of(task1, task2, task3));
        assertThat(chronographData.getTotalTime().toNanos()).isEqualTo(4_404_752_627L);

        // Format the data
        var result = new TableOutputformatter().format(chronographData);

        // Assert the result
        assertThat(result).isEqualToIgnoringWhitespace("""
                My test
                +----------+---------+-------+--------+---------+-----------+---------+---------+---------+
                | Task     | Total   | Count | %      | Median  | Std dev   | Mean    | Min     | Max     |
                +----------+---------+-------+--------+---------+-----------+---------+---------+---------+
                | Quick    | 0.000 s |     5 |   0.0% |  504 ns |     53 ns |  525 ns |  454 ns |  604 ns |
                | Moderate | 0.004 s |     4 |   0.1% | 1.13 ms |  73.95 us | 1.11 ms | 1.00 ms | 1.20 ms |
                | Slow     | 4.400 s |     3 |  99.9% | 1.400 s | 249.44 ms | 1.467 s | 1.200 s | 1.800 s |
                +----------+---------+-------+--------+---------+-----------+---------+---------+---------+
                |      Sum | 4.405 s |    12 | 100.0% |         |           |         |         |         |
                +----------+---------+-------+--------+---------+-----------+---------+---------+---------+""");
    }

}