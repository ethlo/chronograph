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

import com.ethlo.time.internal.util.IndexedCollectionStatistics;

/**
 * Class that holds and calculates performance statistics from an indexed collection of durations.
 * Provides various statistics such as average, median, percentile, min, max, and standard deviation.
 */
public class PerformanceStatistics
{
    private final IndexedCollectionStatistics collectionStatistics;
    private final long totalInvocations;
    private final long totalElapsed;

    /**
     * Constructs a PerformanceStatistics instance with the given collection statistics,
     * total invocations, and total elapsed time.
     *
     * @param collectionStatistics The indexed collection of statistics.
     * @param totalInvocations     The total number of invocations.
     * @param totalElapsed         The total elapsed time in nanoseconds.
     */
    public PerformanceStatistics(final IndexedCollectionStatistics collectionStatistics, long totalInvocations, long totalElapsed)
    {
        this.collectionStatistics = collectionStatistics;
        this.totalInvocations = totalInvocations;
        this.totalElapsed = totalElapsed;
    }

    /**
     * Constructs a PerformanceStatistics instance using the collection statistics' size and sum.
     *
     * @param collectionStatistics The indexed collection of statistics.
     */
    public PerformanceStatistics(IndexedCollectionStatistics collectionStatistics)
    {
        this(collectionStatistics, collectionStatistics.size(), collectionStatistics.sum());
    }

    /**
     * Returns the total number of invocations.
     *
     * @return The total invocations count.
     */
    public long getTotalInvocations()
    {
        return totalInvocations;
    }

    /**
     * Returns whether the collection is empty.
     *
     * @return True if the collection is empty, false otherwise.
     */
    public boolean isEmpty()
    {
        return collectionStatistics.getList().isEmpty();
    }

    /**
     * Returns the total elapsed time as a Duration.
     *
     * @return The total elapsed time.
     */
    public Duration getTotalElapsed()
    {
        return Duration.ofNanos(totalElapsed);
    }

    /**
     * Returns the average elapsed time per invocation as a Duration.
     *
     * @return The average elapsed time.
     */
    public Duration getAverage()
    {
        if (getTotalInvocations() == 0)
        {
            return Duration.ZERO;
        }

        return Duration.ofNanos(BigDecimal.valueOf(getTotalElapsed().toNanos()).divide(BigDecimal.valueOf(getTotalInvocations()), RoundingMode.HALF_UP).longValue());
    }

    /**
     * Returns the median elapsed time as a Duration.
     *
     * @return The median elapsed time.
     */
    public Duration getMedian()
    {
        return getDuration(collectionStatistics.getMedian());
    }

    /**
     * Returns the elapsed time at the specified percentile as a Duration.
     *
     * @param limit The percentile to retrieve (between 0 and 100).
     * @return The elapsed time at the given percentile.
     */
    public Duration getPercentile(double limit)
    {
        return getDuration(collectionStatistics.getPercentile(limit));
    }

    /**
     * Returns the minimum elapsed time as a Duration.
     *
     * @return The minimum elapsed time.
     */
    public Duration getMin()
    {
        return getDuration(collectionStatistics.getMin());
    }

    /**
     * Returns the maximum elapsed time as a Duration.
     *
     * @return The maximum elapsed time.
     */
    public Duration getMax()
    {
        return getDuration(collectionStatistics.getMax());
    }

    /**
     * Returns the standard deviation of the elapsed times as a Duration.
     *
     * @return The standard deviation.
     */
    public Duration getStandardDeviation()
    {
        return getDuration(collectionStatistics.getStandardDeviation());
    }

    private Duration getDuration(Number d)
    {
        return Optional.ofNullable(d).map(dur -> Duration.ofNanos(dur.longValue())).orElse(null);
    }
}