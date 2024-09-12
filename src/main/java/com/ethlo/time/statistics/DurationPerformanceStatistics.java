package com.ethlo.time.statistics;

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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

import com.ethlo.util.IndexedCollection;
import com.ethlo.util.IndexedCollectionStatistics;

public class DurationPerformanceStatistics extends PerformanceStatistics<Duration>
{
    public DurationPerformanceStatistics(final IndexedCollectionStatistics collectionStatistics, long totalInvocations, Duration elapsedTotal)
    {
        super(collectionStatistics, totalInvocations, elapsedTotal.toNanos());
    }

    public DurationPerformanceStatistics(IndexedCollectionStatistics collectionStatistics)
    {
        super(collectionStatistics, collectionStatistics.size(), collectionStatistics.sum());
    }

    @Override
    public Duration getAverage()
    {
        if (getTotalInvocations() == 0)
        {
            return Duration.ZERO;
        }

        return Duration.ofNanos(BigDecimal.valueOf(getElapsedTotal().toNanos()).divide(BigDecimal.valueOf(getTotalInvocations()), RoundingMode.HALF_UP).longValue());
    }

    @Override
    public Duration getMedian()
    {
        return Duration.ofNanos((long) collectionStatistics.getMedian());
    }

    @Override
    public Duration getPercentile(double limit)
    {
        return Duration.ofNanos((long) collectionStatistics.getPercentile(limit));
    }

    @Override
    public Duration getMin()
    {
        return Duration.ofNanos(collectionStatistics.getMin());
    }

    @Override
    public Duration getMax()
    {
        return Duration.ofNanos(collectionStatistics.getMax());
    }

    @Override
    public Duration getStandardDeviation()
    {
        final IndexedCollection<Long> list = collectionStatistics.getList();
        final int count = list.size();
        final long mean = getAverage().toNanos();
        double standardDeviation = 0D;
        for (long num : list)
        {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return Duration.ofNanos((long) Math.sqrt(standardDeviation / (double) count));
    }

    public DurationPerformanceStatistics merge(final DurationPerformanceStatistics other)
    {
        return new DurationPerformanceStatistics(this.collectionStatistics.merge(other.collectionStatistics), totalInvocations + other.totalInvocations, Duration.ofNanos(elapsedTotal + other.elapsedTotal));
    }
}
