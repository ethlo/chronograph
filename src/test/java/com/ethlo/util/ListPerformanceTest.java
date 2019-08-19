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

    private final int size = 2_000_000;
    private final int count = 25;

    @Test
    public void performanceTestLargeLinkedList()
    {
        final Chronograph c = Chronograph.createExtended();
        for (int i = 0; i < count; i++)
        {
            final List<Long> list = c.timedSupplier("add", () -> addLinkedList(size));
            c.timed("sort", () -> list.sort(Comparator.naturalOrder()));
        }

        logger.info(c.prettyPrint("LinkedList", OutputConfig.ALL, TableTheme.SIMPLE));
        assertThat(true).isTrue();
    }

    @Test
    public void performanceTestLargeArrayList()
    {
        final Chronograph c = Chronograph.createExtended();
        for (int i = 0; i < count; i++)
        {
            final List<Long> list = c.timedSupplier("add", () -> addArrayList(size));
            c.timed("sort", () -> list.sort(Comparator.naturalOrder()));
        }

        logger.info(c.prettyPrint("ArrayList", OutputConfig.ALL, TableTheme.SIMPLE));
        assertThat(true).isTrue();
    }

    @Test
    public void performanceTestLargeLongList()
    {
        final Chronograph c = Chronograph.createExtended();
        for (int i = 0; i < count; i++)
        {
            final LongList list = c.timedSupplier("add", () -> addLongList(size));
            c.timed("sort", list::sort);
        }

        logger.info(c.prettyPrint("LongList", OutputConfig.ALL, TableTheme.SIMPLE));
        assertThat(true).isTrue();
    }

    @Test
    public void performanceTestMedium()
    {
        final int size = 500_000;

        final Chronograph c = Chronograph.createExtended();

        for (int i = 0; i < 200; i++)
        {
            c.timed("LinkedList", () -> addLinkedList(size));
            c.timed("ArrayList", () -> addArrayList(size));
            c.timed("LongList", () -> addLongList(size));
        }

        logger.info(c.prettyPrint("None", OutputConfig.ALL, TableTheme.NONE));
        logger.info(c.prettyPrint("Compact", OutputConfig.ALL, TableTheme.COMPACT));
        logger.info(c.prettyPrint("Strong", OutputConfig.ALL, TableTheme.STRONG));
        logger.info(c.prettyPrint("Minimal", OutputConfig.ALL, TableTheme.MINIMAL));
        logger.info(c.prettyPrint("Simple", OutputConfig.ALL, TableTheme.SIMPLE));
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
