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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class LongListTest
{
    private static void assertSorted(LongList a)
    {
        for (int i = 1; i < a.size(); i++)
        {
            if (a.get(i) < a.get(i - 1))
            {
                throw new IllegalArgumentException("Index " + (i - 1) + " should have " + a.get(i) + " < " + a.get(i - 1));
            }
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
        assertThat(new IndexedCollectionStatistics(l).getAverage()).isEqualTo(500);
    }

    @Test
    public void averageLargeNumbers()
    {
        final LongList l = new LongList();
        for (int i = 1; i <= 2_600_000; i++)
        {
            l.add(Integer.MAX_VALUE);
        }
        assertThat(new IndexedCollectionStatistics(l).getAverage()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    public void sortUneven()
    {
        final LongList l = createList(100_101, true);
        l.sort();
        assertSorted(l);
    }

    @Test
    public void testSet()
    {
        final LongList l = createList(100_101, true);
        l.set(100_000, 42L);
        assertThat(l.get(100_000)).isEqualTo(42L);
    }

    @Test
    public void sortEven()
    {
        final LongList l = createList(100_000, true);
        l.sort();
        assertSorted(l);
    }

    @Test
    public void medianEven()
    {
        final LongList l = createList(100_000, true);
        assertThat(new IndexedCollectionStatistics(l).getMedian()).isEqualTo(49_999L);
    }

    @Test
    public void medianOdd()
    {
        final LongList l = createList(100_001, true);
        assertThat(new IndexedCollectionStatistics(l).getMedian()).isEqualTo(50_000);
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
        final LongList l = createList(100_001, true);
        assertThat(new IndexedCollectionStatistics(l).getPercentile(90D)).isEqualTo(90_000);
    }

    @Test
    public void getOutOfBounds()
    {
        final LongList l = createList(10, false);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> l.get(10));
    }

    @Test
    public void getOutOfBoundsNegative()
    {
        final LongList l = createList(10, false);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> l.get(-1));
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

    @Test
    void size()
    {
        final LongList l = createList(10, true);
        assertThat(l.size()).isEqualTo(10);
    }
}
