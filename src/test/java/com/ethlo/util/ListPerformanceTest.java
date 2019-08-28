package com.ethlo.util;

/*-
 * #%L
 * Chronograph
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ethlo.ascii.TableTheme;
import com.ethlo.time.CaptureConfig;
import com.ethlo.time.Chronograph;
import com.ethlo.time.ChronographData;
import com.ethlo.time.OutputConfig;
import com.ethlo.time.PresentationMode;
import com.ethlo.time.Report;

public class ListPerformanceTest
{
    private static final Logger logger = LoggerFactory.getLogger(ListPerformanceTest.class);

    private final int size = 5_000_000;
    private final int count = 4;

    @Test
    public void performanceTestLargeLinkedList()
    {
        final Chronograph c = Chronograph.create();
        for (int i = 0; i < count; i++)
        {
            final List<Long> list = c.timedSupplier("add", () -> addLinkedList(size));
            c.timed("sort", () -> list.sort(Comparator.naturalOrder()));
        }

        logger.info(c.prettyPrint("LinkedList"));
        assertThat(true).isTrue();
    }

    @Test
    public void performanceTestLargeArrayList()
    {
        final Chronograph c = Chronograph.create();
        for (int i = 0; i < count; i++)
        {
            final List<Long> list = c.timedSupplier("add", () -> addArrayList(size));
            c.timed("sort", () -> list.sort(Comparator.naturalOrder()));
        }

        logger.info(c.prettyPrint("ArrayList"), new OutputConfig().mode(PresentationMode.THROUGHPUT));
        assertThat(true).isTrue();
    }

    @Test
    public void performanceTestLargeLongList()
    {
        final Chronograph c = Chronograph.create();
        for (int i = 0; i < count; i++)
        {
            final LongList list = c.timedSupplier("add", () -> addLongList(size));
            c.timed("sort", list::sort);
        }

        logger.info(c.prettyPrint("LongList"));
        assertThat(true).isTrue();
    }

    @Test
    public void rateLimitingTest()
    {
        final Chronograph c = Chronograph.create(CaptureConfig.minInterval(Duration.ofNanos(1000)));

        final IndexedCollection<Long> list = new LongList(100_000);
        for (int i = 0; i < 2_000_000; i++)
        {
            c.timed("Warmup", () -> list.add(randomNano()));
        }
        for (int i = 0; i < 10_000_000; i++)
        {
            c.timed("Adding", () -> list.add(randomNano()));
        }

        System.out.println(Report.prettyPrint(c.getTaskData(), OutputConfig.DEFAULT.mode(PresentationMode.THROUGHPUT).formatting(false).percentiles(90, 95, 99, 99.9), TableTheme.TSV));

        assertThat(true).isTrue();
    }

    @Test
    public void performanceTestMediumAdd()
    {
        final Chronograph c = performAddBenchmark(10, 500_000);
        output(c, TableTheme.RED_HERRING);
        assertThat(true).isTrue();
    }

    private Chronograph performAddBenchmark(final int runs, final int size)
    {
        final Chronograph c = Chronograph.create("Add");

        for (int i = 0; i < runs; i++)
        {
            c.timedFunction("LinkedList", this::addLinkedList, size);
            c.timedFunction("ArrayList", this::addArrayList, size);
            c.timedFunction("IndexedCollection", this::addLongList, size);
        }
        return c;
    }

    @Test
    public void performanceTestMediumSort()
    {
        final Chronograph c = performSortBenchmark(10, 500_000);
        output(c, TableTheme.RED_HERRING);
        output(c, TableTheme.DOUBLE);
        output(c, TableTheme.DEFAULT);
        output(c, TableTheme.ROUNDED);
        output(c, TableTheme.MINIMAL);
        output(c, TableTheme.COMPACT);
        assertThat(true).isTrue();
    }

    private Chronograph performSortBenchmark(final int runs, final int size)
    {
        final Chronograph c = Chronograph.create("Sort");

        for (int i = 0; i < runs; i++)
        {
            final IndexedCollection<Long> longList = addLongList(size);
            final List<Long> linkedList = addLinkedList(size);
            final List<Long> arrayList = addArrayList(size);

            c.timed("Linkedlist", () -> linkedList.sort(Comparator.naturalOrder()));
            c.timed("Arraylist", () -> arrayList.sort(Comparator.naturalOrder()));
            c.timed("IndexedCollection", longList::sort);
        }
        return c;
    }

    @Test
    public void testCombinedPerformanceTable()
    {
        final Chronograph a = performAddBenchmark(20, 10_000);
        final Chronograph b = performSortBenchmark(10, 10_000);
        final ChronographData combined = ChronographData.combine("Combined", Arrays.asList(a, b));

        System.out.println(Report.prettyPrint(combined,
                OutputConfig.EXTENDED.mode(PresentationMode.THROUGHPUT).benchmarkMode(true),
                TableTheme.RED_HERRING
        ));

        assertThat(true).isTrue();
    }


    private void output(final Chronograph c, TableTheme theme)
    {
        System.out.println(Report.prettyPrint(c.getTaskData(), OutputConfig.EXTENDED.benchmarkMode(true), theme));
    }

    private LongList addLongList(int count)
    {
        final LongList l = new LongList();
        for (long i = 0; i < count; i++)
        {
            l.add(randomNano());
        }
        return l;
    }

    private List<Long> addLinkedList(int count)
    {
        final List<Long> l = new LinkedList<>();
        for (long i = 0; i < count; i++)
        {
            l.add(randomNano());
        }
        return l;
    }

    private List<Long> addArrayList(int count)
    {
        final List<Long> l = new ArrayList<>();
        for (long i = 0; i < count; i++)
        {
            l.add(randomNano());
        }
        return l;
    }

    private long randomNano()
    {
        return (long) (Math.random() * Long.MAX_VALUE);
    }
}
