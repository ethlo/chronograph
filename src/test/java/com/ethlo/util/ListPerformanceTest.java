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
import com.ethlo.time.Report;
import com.ethlo.time.TaskPerformanceStatistics;

public class ListPerformanceTest
{
    private static final Logger logger = LoggerFactory.getLogger(ListPerformanceTest.class);

    private final int size = 5_000_000;
    private final int count = 10;

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

        logger.info(c.prettyPrint("ArrayList"));
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
        final Chronograph c = Chronograph.create(CaptureConfig.builder().minInterval(Duration.ofMillis(1)).build());

        final IndexedCollection<Long> list = new LongList(100_000);
        for (int i = 0; i < 10_000_000; i++)
        {
            final int finalI = i;
            c.timed("Initial add", () -> doAdd(list, finalI));
        }
        for (int i = 0; i < 40_000_000; i++)
        {
            final int finalI = i;
            c.timed("Single add", () -> doAdd(list, finalI));
        }

        System.out.println(Report.prettyPrint(c.getTaskData(), OutputConfig.DEFAULT.begin().percentiles(90, 95, 99, 99.9).build(), TableTheme.DEFAULT));
    }

    private void doAdd(final IndexedCollection<Long> list, final long value)
    {
        list.add(value);
    }

    @Test
    public void performanceTestMediumAdd()
    {
        final int size = 50_000;
        final Chronograph c = performAddBenchmark(size);
        output(c, TableTheme.RED_HERRING);
        assertThat(true).isTrue();
    }

    private Chronograph performAddBenchmark(final int size)
    {
        final Chronograph c = Chronograph.create("Add");

        for (int i = 0; i < 100; i++)
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
        final int size = 500_000;
        final Chronograph c = performSortBenchmark(size);
        output(c, TableTheme.RED_HERRING);
        assertThat(true).isTrue();
    }

    private Chronograph performSortBenchmark(final int size)
    {
        final Chronograph c = Chronograph.create("Sort");

        for (int i = 0; i < 100; i++)
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
        final Chronograph a = performAddBenchmark(10_000);
        final Chronograph b = performSortBenchmark(10_000);
        final List<TaskPerformanceStatistics> aa = a.getTaskData().getTaskStatistics();
        final List<TaskPerformanceStatistics> bb = b.getTaskData().getTaskStatistics();
        aa.addAll(bb);
        System.out.println(Report.prettyPrint(new ChronographData("Combined", aa, a.getTotalTime().plus(b.getTotalTime())), OutputConfig.EXTENDED, TableTheme.RED_HERRING));
        assertThat(true).isTrue();
    }


    private void output(final Chronograph c, TableTheme theme)
    {
        System.out.println(Report.prettyPrint(c.getTaskData(), OutputConfig.EXTENDED.begin().benchmarkMode(true).build(), theme));
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
