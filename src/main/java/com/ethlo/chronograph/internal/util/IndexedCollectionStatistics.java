package com.ethlo.chronograph.internal.util;

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

import java.math.BigInteger;

public class IndexedCollectionStatistics
{
    private final IndexedCollection<Long> list;
    private final long sum;

    public IndexedCollectionStatistics(IndexedCollection<Long> list)
    {
        this.list = list;
        list.sort();
        this.sum = calculateSum();
    }

    private long calculateSum()
    {
        long sum = 0;
        for (long l : list)
        {
            sum += l;
        }
        return sum;
    }

    public Long getMin()
    {
        return isEmpty() ? null : list.get(0);
    }

    public Long getMax()
    {
        return isEmpty() ? null : list.get(list.size() - 1);
    }

    public Long getAverage()
    {
        if (isEmpty())
        {
            return null;
        }

        BigInteger sum = BigInteger.ZERO;
        for (final Long l : list)
        {
            final BigInteger bi = BigInteger.valueOf(l);
            sum = sum.add(bi);
        }
        return sum.divide(BigInteger.valueOf(list.size())).longValue();
    }

    public Long getPercentile(double percentile)
    {
        if (isEmpty())
        {
            return null;
        }

        final int index = (int) Math.ceil((percentile / 100) * list.size());
        return list.get(index - 1);
    }

    public Long getMedian()
    {
        if (list.isEmpty())
        {
            return null;
        }

        final int pivot = list.size() / 2;
        if (pivot * 2 == list.size())
        {
            // Average of two middle elements
            return (list.get(pivot - 1) + list.get(pivot)) / 2;
        }
        return list.get(pivot);
    }

    public long sum()
    {
        return sum;
    }

    public long size()
    {
        return list.size();
    }

    public IndexedCollection<Long> getList()
    {
        return list;
    }

    public IndexedCollectionStatistics merge(IndexedCollectionStatistics other)
    {
        final LongList list = new LongList((Math.max(10, this.list.size() + other.list.size()) / 10));
        this.list.forEach(list::add);
        other.list.forEach(list::add);
        return new IndexedCollectionStatistics(list);
    }

    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    public Long getStandardDeviation()
    {
        if (isEmpty())
        {
            return null;
        }

        final int count = list.size();
        final Long mean = getAverage();
        double standardDeviation = 0D;
        for (long num : list)
        {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return (long) Math.sqrt(standardDeviation / count);
    }

    public long getSum()
    {
        return sum;
    }
}
