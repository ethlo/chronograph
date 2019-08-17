package com.ethlo.util;

import static java.math.BigDecimal.ROUND_HALF_UP;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LongList implements Iterable<Long>
{
    private final int blockSize = 10000;
    private int index = 0;

    private final List<long[]> blocks = new ArrayList<>(1);
    private boolean isSorted = false;

    public void add(long l)
    {
        if (index % blockSize == 0)
        {
            blocks.add(new long[blockSize]);
        }
        final int blockIndex = index / blockSize;
        blocks.get(blockIndex)[index % blockSize] = l;
        index++;

        isSorted = false;
    }

    public double getAverage()
    {
        BigInteger sum = BigInteger.ZERO;
        for (final Long aLong : this)
        {
            final BigInteger bi = BigInteger.valueOf(aLong);
            sum = sum.add(bi);
        }
        return sum.divide(BigInteger.valueOf(index)).doubleValue();
    }

    public void shuffle()
    {
        final int size = size();
        for (int i = 0; i < size; i++)
        {
            int from = (int) (Math.random() * size);
            final long a = get(from);
            final long b = get(i);
            set(i, a);
            set(from, b);
        }
    }

    public double percentile(double percentile)
    {
        sort();
        final int index = (int) Math.ceil((percentile / 100) * size());
        return get(index - 1);
    }

    public double median()
    {
        sort();

        final int pivot = size() / 2;
        if (pivot * 2 == size())
        {
            // Average of two middle elements
            return (get(pivot - 1) + get(pivot)) / 2D;
        }
        return get(pivot);
    }

    public void sort()
    {
        if (!isSorted)
        {
            shuffle();
            QuickDualPivot.sort(this);
            this.isSorted = true;
        }
    }

    public long get(int index)
    {
        if (index < 0 || index > this.index)
        {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        final int blockIndex = index / blockSize;
        return blocks.get(blockIndex)[index % blockSize];
    }

    @Override
    public Iterator<Long> iterator()
    {
        return new Iterator<Long>()
        {
            private int idx = 0;

            @Override
            public boolean hasNext()
            {
                return idx < index;
            }

            @Override
            public Long next()
            {
                return get(idx++);
            }
        };
    }

    public int size()
    {
        return this.index;
    }

    public void set(final int index, final long l)
    {
        final int blockIndex = index / blockSize;
        blocks.get(blockIndex)[index % blockSize] = l;
        isSorted = false;
    }

    public long getMin()
    {
        if (isSorted)
        {
            return get(0);
        }
        else
        {
            long min = Long.MAX_VALUE;
            for (long l : this)
            {
                min = Math.min(l, min);
            }
            return min;
        }
    }

    public long getMax()
    {
        if (isSorted)
        {
            return get(index - 1);
        }
        else
        {
            long max = Long.MIN_VALUE;
            for (long l : this)
            {
                max = Math.max(l, max);
            }
            return max;
        }
    }

    public double getStandardDeviation()
    {
        final int count = size();
        final double average = getAverage();
        BigDecimal sd = BigDecimal.valueOf(0);
        for (long l : this)
        {
            final double val = Math.pow( (l - average) / (double) count, 2);
            sd = sd.add(BigDecimal.valueOf(val));
        }
        return sqrt(sd, 10).doubleValue();
    }

    public static BigDecimal sqrt(BigDecimal value, final int SCALE)
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
}
