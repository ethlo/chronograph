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

import static java.math.BigDecimal.ROUND_HALF_UP;

import java.math.BigDecimal;
import java.math.BigInteger;

public class IndexedCollectionStatistics
{
    private final IndexedCollection<Long> list;
    private final long sum;

    public IndexedCollectionStatistics(IndexedCollection<Long> list)
    {
        this.list = list;
        list.sort();
        this.sum =  calculateSum();
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

    public double getStandardDeviation()
    {
        final int count = list.size();
        final double average = getAverage();
        BigDecimal sd = BigDecimal.valueOf(0);
        for (long l : list)
        {
            final double val = Math.pow((l - average) / (double) count, 2);
            sd = sd.add(BigDecimal.valueOf(val));
        }
        return sqrt(sd, 10).doubleValue();
    }

    private BigDecimal sqrt(BigDecimal value, final int SCALE)
    {
        BigDecimal TWO = BigDecimal.valueOf(2);
        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1 = new BigDecimal(Math.sqrt(value.doubleValue()));
        while (!x0.equals(x1))
        {
            x0 = x1;
            x1 = value.divide(x0, SCALE, ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(TWO, SCALE, ROUND_HALF_UP);

        }
        return x1;
    }

    public long getMin()
    {
        return list.get(0);
    }

    public long getMax()
    {
        return list.get(list.size() - 1);
    }

    public double getAverage()
    {
        BigInteger sum = BigInteger.ZERO;
        for (final Long l : list)
        {
            final BigInteger bi = BigInteger.valueOf(l);
            sum = sum.add(bi);
        }
        return sum.divide(BigInteger.valueOf(list.size())).doubleValue();
    }

    public double getPercentile(double percentile)
    {
        final int index = (int) Math.ceil((percentile / 100) * list.size());
        return list.get(index - 1);
    }

    public double getMedian()
    {
        final int pivot = list.size() / 2;
        if (pivot * 2 == list.size())
        {
            // Average of two middle elements
            return (list.get(pivot - 1) + list.get(pivot)) / 2D;
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
}