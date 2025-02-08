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
import java.util.Optional;

import com.ethlo.util.IndexedCollectionStatistics;

public class PerformanceStatistics
{
    private final IndexedCollectionStatistics collectionStatistics;
    private final long totalInvocations;
    private final long elapsedTotal;

    public PerformanceStatistics(final IndexedCollectionStatistics collectionStatistics, long totalInvocations, long elapsedTotal)
    {
        this.collectionStatistics = collectionStatistics;
        this.totalInvocations = totalInvocations;
        this.elapsedTotal = elapsedTotal;
    }

    public PerformanceStatistics(IndexedCollectionStatistics collectionStatistics)
    {
        this(collectionStatistics, collectionStatistics.size(), collectionStatistics.sum());
    }

    public long getTotalInvocations()
    {
        return totalInvocations;
    }

    public boolean isEmpty()
    {
        return collectionStatistics.getList().isEmpty();
    }

    public Duration getElapsedTotal()
    {
        return Duration.ofNanos(elapsedTotal);
    }


    public Duration getAverage()
    {
        if (getTotalInvocations() == 0)
        {
            return Duration.ZERO;
        }

        return Duration.ofNanos(BigDecimal.valueOf(getElapsedTotal().toNanos()).divide(BigDecimal.valueOf(getTotalInvocations()), RoundingMode.HALF_UP).longValue());
    }

    public Duration getMedian()
    {
        return getDuration(collectionStatistics.getMedian());
    }

    public Duration getPercentile(double limit)
    {
        return getDuration(collectionStatistics.getPercentile(limit));
    }

    public Duration getMin()
    {
        return getDuration(collectionStatistics.getMin());
    }

    public Duration getMax()
    {
        return getDuration(collectionStatistics.getMax());
    }

    public Duration getStandardDeviation()
    {
        return getDuration(collectionStatistics.getStandardDeviation());
    }

    private Duration getDuration(Number d)
    {
        return Optional.ofNullable(d).map(dur -> Duration.ofNanos(dur.longValue())).orElse(null);
    }
}