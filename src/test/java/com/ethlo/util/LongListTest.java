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
import static org.assertj.core.api.Assertions.fail;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ethlo.ascii.TableTheme;
import com.ethlo.time.Chronograph;
import com.ethlo.time.OutputConfig;
import com.ethlo.time.Report;

public class LongListTest
{
    private static final Logger logger = LoggerFactory.getLogger(LongListTest.class);

    @Test
    public void performanceTestMedium()
    {
        final int size = 100_000;

        final Chronograph c = Chronograph.createExtended();

        for (int i = 0; i < 1_000; i++)
        {
            c.timed("LinkedList", () -> addLinkedList(size));
            c.timed("ArrayList", () -> addArrayList(size));
            c.timed("LongList", () -> createList(size, true));
        }

        logger.info(c.prettyPrint("Medium test (100k entries)", OutputConfig.ALL, TableTheme.COMPACT));
        assertThat(true).isTrue();
    }

    @Test
    public void performanceTestLargeLinkedList()
    {
        final int size = 20_000_000;
        final int count = 10;

        final Chronograph c = Chronograph.createExtended();
        for (int i = 0; i < count; i++)
        {
            c.timed("add", () -> addLinkedList(size));
        }

        logger.info(c.prettyPrint("LinkedList", OutputConfig.ALL, TableTheme.STRONG));
        assertThat(true).isTrue();
    }

    @Test
    public void performanceTestLargeArrayList()
    {
        final int size = 20_000_000;
        final int count = 10;

        final Chronograph c = Chronograph.createExtended();
        for (int i = 0; i < count; i++)
        {
            c.timed("add", () -> addArrayList(size));
        }

        logger.info(c.prettyPrint("ArrayList", OutputConfig.ALL, TableTheme.NONE));
        assertThat(true).isTrue();
    }

    @Test
    public void performanceTestLargeLongList()
    {
        final int size = 20_000_000;
        final int count = 10;

        final Chronograph c = Chronograph.createExtended();
        for (int i = 0; i < count; i++)
        {
            c.timed("add", () -> addLongList(size));
        }

        logger.info(c.prettyPrint("LongList", OutputConfig.ALL, TableTheme.NONE));
        assertThat(true).isTrue();
    }

    private void addLongList(int count)
    {
        final LongList l = new LongList();
        for (long i = 0; i < count; i++)
        {
            l.add(i);
        }
    }

    private void addLinkedList(int count)
    {
        final List<Long> l = new LinkedList<>();
        for (long i = 0; i < count; i++)
        {
            l.add(i);
        }
    }

    private void addArrayList(int count)
    {
        final List<Long> l = new ArrayList<>();
        for (long i = 0; i < count; i++)
        {
            l.add(i);
        }
    }

    @Test
    public void averageSmall()
    {
        final LongList l = new LongList();
        for (int i = 1; i <= 1000; i++)
        {
            l.add(i);
        }
        assertThat(l.getAverage()).isEqualTo(500.0D);
    }

    @Test
    public void averageLargeNumbers()
    {
        final LongList l = new LongList();
        for (int i = 1; i <= 2_600_000; i++)
        {
            l.add(Integer.MAX_VALUE);
        }
        assertThat(l.getAverage()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    public void sort()
    {
        final LongList l = createList(1_000_000, true);
        final Chronograph c = Chronograph.create();
        c.timed("sort", l::sort);
        assertThat(isSorted(l)).isTrue();
        logger.info(c.prettyPrint());
    }

    @Test
    public void medianEven()
    {
        final LongList l = createList(100_000, true);
        assertThat(l.getMedian()).isEqualTo(49_999.5);
    }

    @Test
    public void medianOdd()
    {
        final LongList l = createList(100_001, true);
        assertThat(l.getMedian()).isEqualTo(50_000);
    }

    private LongList createList(int size, boolean reverseSorted)
    {
        final LongList l = new LongList();
        for (long i = size - 1; i >= 0; i--)
        {
            l.add(reverseSorted ? i : (long) (Math.random() + Long.MAX_VALUE));
        }
        return l;
    }

    @Test
    public void percentile()
    {
        final LongList l = createList(100, true);
        assertThat(l.getPercentile(90D)).isEqualTo(89);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getOutOfBounds()
    {
        final LongList l = createList(10, false);
        l.get(10);
        fail("Should throw");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getOutOfBoundsNegative()
    {
        final LongList l = createList(10, false);
        l.get(-1);
        fail("Should throw");
    }


    @Test
    public void get()
    {
        final int size = 25_000_005;
        final LongList l = createList(size, true);
        assertThat(l.size()).isEqualTo(size);

        assertThat(l.get(0)).isEqualTo(size - 1);
        assertThat(l.get(9)).isEqualTo(size - 10);
        assertThat(l.get(24_999_999)).isEqualTo(size - 25_000_000);
    }

    @Test
    public void iterator()
    {
        final LongList l = createList(10, true);
        l.sort();
        int count = 0;
        for (long a : l)
        {
            assertThat(a).isEqualTo(count);
            count++;
        }
        assertThat(count).isEqualTo(10);
    }

    private static boolean isSorted(LongList a)
    {
        return isSorted(a, 0, a.size() - 1);
    }

    private static boolean isSorted(LongList a, int lo, int hi)
    {
        for (int i = lo + 1; i <= hi; i++)
        {
            if (a.get(i) < a.get(i - 1))
            {
                return false;
            }
        }
        return true;
    }
}
