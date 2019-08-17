package com.ethlo.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ethlo.util.LongList;

public class LongListTest
{
    private static final Logger logger = LoggerFactory.getLogger(LongListTest.class);

    @Test
    public void performanceTestMedium()
    {
        final int size = 100_000;

        final Chronograph c = Chronograph.create();

        for (int i = 0; i < 1_000; i++)
        {
            c.timed("Add LongList", () -> createList(size, true));
            c.timed("Add LinkedList", () -> addLinkedList(size));
            c.timed("Add ArrayList", () -> addArrayList(size));
        }

        logger.info(c.prettyPrint());
    }

    @Test
    public void performanceTestLarge()
    {
        final int size = 10_000_000;

        final Chronograph c = Chronograph.create();

        for (int i = 0; i < 5; i++)
        {
            c.timed("Add LongList", () -> createList(size, true));
            c.timed("Add LinkedList", () -> addLinkedList(size));
            c.timed("Add ArrayList", () -> addArrayList(size));
        }

        logger.info(c.prettyPrint());
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
        assertThat(l.average()).isEqualTo(500.0D);
    }

    @Test
    public void averageLargeNumbers()
    {
        final LongList l = new LongList();
        for (int i = 1; i <= 2_600_000; i++)
        {
            l.add(Integer.MAX_VALUE);
        }
        assertThat(l.average()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    public void sort()
    {
        final LongList l = createList(1_000_000, true);
        final ArrayList<Long> al = new ArrayList<>();
        final Chronograph c = Chronograph.create();
        c.timed("sort", l::sort);
        logger.info(c.prettyPrint());
    }

    @Test
    public void median()
    {
        final LongList l = createList(100, true);
        assertThat(l.median()).isEqualTo(49.5);
    }

    private LongList createList()
    {
        return createList(100_000, false);
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
        assertThat(l.percentile(90D)).isEqualTo(89);
    }

    @Test
    public void min()
    {
        final LongList l = createList(100, true);
        assertThat(l.getMin()).isEqualTo(0);
    }

    @Test
    public void max()
    {
        final LongList l = createList(100, true);
        assertThat(l.getMax()).isEqualTo(99);
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
    }
}