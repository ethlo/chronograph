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

import org.junit.jupiter.api.Test;

import com.ethlo.time.CaptureConfig;
import com.ethlo.time.Chronograph;
import com.ethlo.time.ChronographData;
import com.ethlo.time.internal.util.IndexedCollection;
import com.ethlo.time.internal.util.LongList;
import com.ethlo.time.output.table.TableOutputFormatter;
import com.ethlo.time.output.table.TableThemes;

class ListPerformanceTest extends BaseTest
{
    private final int size = 2_000_000;
    private final int count = 4;

    @Test
    void performanceTestLargeLinkedList()
    {
        final Chronograph c = Chronograph.create();
        for (int i = 0; i < count; i++)
        {
            final List<Long> list = c.time("add", () -> addLinkedList(size));
            c.time("sort", () -> list.sort(Comparator.naturalOrder()));
        }

        output(c);
        assertThat(true).isTrue();
    }

    @Test
    void performanceTestLargeArrayList()
    {
        final Chronograph c = Chronograph.create();
        for (int i = 0; i < count; i++)
        {
            final List<Long> list = c.time("add", () -> addArrayList(size));
            c.time("sort", () -> list.sort(Comparator.naturalOrder()));
        }

        output(c);
        assertThat(true).isTrue();
    }

    @Test
    void performanceTestLargeLongList()
    {
        final Chronograph c = Chronograph.create();
        for (int i = 0; i < count; i++)
        {
            final LongList list = c.time("add", () -> addLongList(size));
            c.time("sort", list::sort);
        }

        output(c);
        assertThat(true).isTrue();
    }

    @Test
    void rateLimitingTest()
    {
        final Chronograph c = Chronograph.create(CaptureConfig.minInterval(Duration.ofNanos(10_000)));

        final IndexedCollection<Long> list = new LongList(100_000);
        for (int i = 0; i < 2_000_000; i++)
        {
            c.time("Warmup", () -> list.add(randomNano()));
        }
        for (int i = 0; i < 10_000_000; i++)
        {
            c.time("Adding", () -> list.add(randomNano()));
        }

        output(c);

        assertThat(true).isTrue();
    }

    @Test
    void performanceTestMediumAdd()
    {
        final Chronograph c = performAddBenchmark(10, 500_000);
        output(c, TableThemes.RED_HERRING);
        assertThat(true).isTrue();
    }

    private Chronograph performAddBenchmark(final int runs, final int size)
    {
        final Chronograph c = Chronograph.create("Add");

        for (int i = 0; i < runs; i++)
        {
            c.time("LinkedList - Add", this::addLinkedList, size);
            c.time("ArrayList - Add", this::addArrayList, size);
            c.time("IndexedCollection - Add", this::addLongList, size);
        }
        return c;
    }

    @Test
    void performanceTestMediumSort()
    {
        final Chronograph c = performSortBenchmark(10, 500_000);
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

            c.time("LinkedList - Sort", () -> linkedList.sort(Comparator.naturalOrder()));
            c.time("ArrayList - Sort", () -> arrayList.sort(Comparator.naturalOrder()));
            c.time("IndexedCollection - Sort", longList::sort);
        }
        return c;
    }

    @Test
    void testCombinedPerformanceTable()
    {
        final Chronograph a = performAddBenchmark(20, 10_000);
        final Chronograph b = performAddBenchmark(10, 10_000);
        final Chronograph c = performAddBenchmark(5, 10_000);
        final Chronograph d = performSortBenchmark(8, 10_000);
        final ChronographData combined = ChronographData.merge("Combined", Arrays.asList(a, b, c, d));

        System.out.println(new TableOutputFormatter().format(combined));

        assertThat(true).isTrue();
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
