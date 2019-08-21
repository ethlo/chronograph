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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ethlo.ascii.TableTheme;
import com.ethlo.time.Chronograph;
import com.ethlo.time.OutputConfig;

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
    public void performanceTestMedium()
    {
        Chronograph.configure(TableTheme.SIMPLE, OutputConfig.EXTENDED);
        final int size = 500_000;

        final Chronograph c = Chronograph.create();

        for (int i = 0; i < 100; i++)
        {
            final List<Long> linkedList = c.timedFunction("LinkedList add", this::addLinkedList, size);
            c.timed("Linkedlist sort", () -> linkedList.sort(Comparator.naturalOrder()));

            final List<Long> arrayList = c.timedFunction("ArrayList add", this::addArrayList, size);
            c.timed("Arraylist sort", () -> arrayList.sort(Comparator.naturalOrder()));

            final LongList longList = c.timedFunction("LongList add", this::addLongList, size);
            c.timed("LongList sort", longList::sort);
        }

        Chronograph.configure(TableTheme.NONE);
        System.out.println(c.prettyPrint("None"));
        Chronograph.configure(TableTheme.COMPACT);
        System.out.println(c.prettyPrint("Compact"));
        Chronograph.configure(TableTheme.STRONG);
        System.out.println(c.prettyPrint("Strong"));
        Chronograph.configure(TableTheme.SIMPLE);
        System.out.println(c.prettyPrint("Simple"));
        Chronograph.configure(TableTheme.BRIGHT);
        System.out.println(c.prettyPrint("Bright"));
        Chronograph.configure(TableTheme.MINIMAL);
        System.out.println(c.prettyPrint("Minimal"));

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
