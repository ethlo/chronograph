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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class LongList implements IndexedCollection<Long>
{
    private static final int DEFAULT_BLOCK_SIZE = 1_000;

    private final int blockSize;
    private final List<long[]> blocks = new ArrayList<>(16);
    private int index = 0;
    private boolean isSorted = false;

    public LongList()
    {
        this(DEFAULT_BLOCK_SIZE);
    }

    public LongList(int blockSize)
    {
        if (blockSize < 1)
        {
            throw new IllegalArgumentException("blockSize cannot be less than 1");
        }
        this.blockSize = blockSize;
    }

    @Override
    public void add(long l)
    {
        if (index == Integer.MAX_VALUE)
        {
            throw new IndexOutOfBoundsException("Cannot add more than " + Integer.MAX_VALUE + " entries");
        }
        if (index % blockSize == 0)
        {
            blocks.add(new long[blockSize]);
        }
        final int blockIndex = index / blockSize;
        blocks.get(blockIndex)[index % blockSize] = l;
        index++;

        isSorted = false;
    }

    @Override
    public Long get(int index)
    {
        if (index < 0 || index >= this.index)
        {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        final int blockIndex = index / blockSize;
        return blocks.get(blockIndex)[index % blockSize];
    }

    @Override
    public int size()
    {
        return this.index;
    }

    @Override
    public void set(final int index, final Long l)
    {
        final int blockIndex = index / blockSize;
        blocks.get(blockIndex)[index % blockSize] = Objects.requireNonNull(l);
        isSorted = false;
    }

    @Override
    public Iterator<Long> iterator()
    {
        return new Iterator<>()
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

    @Override
    public void sort()
    {
        if (!isSorted)
        {
            final long[] all = new long[size()];
            for (int i = 0; i < blocks.size(); i++)
            {
                final long[] src = blocks.get(i);
                final int offset = i * blockSize;
                final boolean isLast = i == blocks.size() - 1;
                final int size = isLast ? index % blockSize == 0 ? blockSize : index % blockSize : blockSize;
                System.arraycopy(src, 0, all, offset, size);
            }
            Arrays.sort(all);

            for (int i = 0; i < blocks.size(); i++)
            {
                final long[] target = blocks.get(i);
                final int offset = i * blockSize;
                final boolean isLast = i == blocks.size() - 1;
                final int size = isLast ? index % blockSize == 0 ? blockSize : index % blockSize : blockSize;
                System.arraycopy(all, offset, target, 0, size);
            }

            this.isSorted = true;
        }
    }

    @Override
    public Stream<Long> stream()
    {
        final Iterable<Long> iterable = LongList.this;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    @Override
    public LongList addAll(final Iterable<Long> values)
    {
        values.forEach(this::add);
        return this;
    }

    public long sum()
    {
        long sum = 0;
        for (long l : this)
        {
            sum += l;
        }
        return sum;
    }

    @Override
    public boolean isEmpty()
    {
        return size() == 0;
    }
}
