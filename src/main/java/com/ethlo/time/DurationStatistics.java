package com.ethlo.time;

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

import com.ethlo.util.IndexedCollectionStatistics;

public class DurationStatistics
{
    private final IndexedCollectionStatistics collectionStatistics;
    private final long totalInvocations;
    private final Duration elapsedTotal;

    public DurationStatistics(final IndexedCollectionStatistics collectionStatistics, long totalInvocations, Duration elapsedTotal)
    {
        this.collectionStatistics = collectionStatistics;
        this.totalInvocations = totalInvocations;
        this.elapsedTotal = elapsedTotal;
    }

    public Duration getTotal()
    {
        return elapsedTotal;
    }

    public Duration getAverage()
    {
        if (totalInvocations == 0)
        {
            return Duration.ZERO;
        }

        return Duration.ofNanos(BigDecimal.valueOf(elapsedTotal.toNanos()).divide(BigDecimal.valueOf(totalInvocations), RoundingMode.HALF_UP).longValue());
    }

    public Duration getMedian()
    {
        return collectionStatistics != null ? Duration.ofNanos((long) collectionStatistics.getMedian()) : Duration.ZERO;
    }

    public Duration getPercentile(double limit)
    {
        return collectionStatistics != null ? Duration.ofNanos((long) collectionStatistics.getPercentile(limit)) : Duration.ZERO;
    }

    public Duration getMin()
    {
        return Duration.ofNanos(collectionStatistics.getMin());
    }

    public Duration getMax()
    {
        return Duration.ofNanos(collectionStatistics.getMax());
    }

    public Duration getStandardDeviation()
    {
        return collectionStatistics != null ? Duration.ofNanos((long) collectionStatistics.getStandardDeviation()) : Duration.ZERO;
    }
}
