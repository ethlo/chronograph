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
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Duration;

import com.ethlo.util.IndexedCollection;
import com.ethlo.util.IndexedCollectionStatistics;
import com.ethlo.util.MathUtil;

public class ThroughputPerformanceStatistics extends PerformanceStatistics<Double>
{
    private static final double D_NANOS = 1_000_000_000D;

    public ThroughputPerformanceStatistics(final IndexedCollectionStatistics collectionStatistics, long totalInvocations, Duration elapsedTotal)
    {
        super(collectionStatistics, totalInvocations, elapsedTotal);
    }

    @Override
    public Double getAverage()
    {
        if (getTotalInvocations() == 0)
        {
            return Double.NaN;
        }
        return divide(totalInvocations, elapsedTotal.toNanos());
    }

    private Double divide(final long events, final long nanos)
    {
        final BigDecimal second = BigDecimal.valueOf(nanos).divide(BigDecimal.valueOf(D_NANOS), MathContext.DECIMAL128);
        return BigDecimal.valueOf(events).divide(second, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public Double getMedian()
    {
        return D_NANOS / collectionStatistics.getMedian();
    }

    @Override
    public Double getPercentile(double limit)
    {
        return D_NANOS / collectionStatistics.getPercentile(limit);
    }

    @Override
    public Double getMin()
    {
        // Min is where time is at the highest
        final long slowestNanos = collectionStatistics.getMax();
        return D_NANOS / slowestNanos;
    }

    @Override
    public Double getMax()
    {
        // Max is where time is at the lowest
        final long fastestNanos = collectionStatistics.getMin();
        return D_NANOS / fastestNanos;
    }

    @Override
    public Double getStandardDeviation()
    {
        final IndexedCollection<Long> list = collectionStatistics.getList();
        final int count = list.size();
        if (count == 0)
        {
            return 0D;
        }
        final double average = getAverage();
        BigDecimal sd = BigDecimal.valueOf(0);
        for (long l : list)
        {
            final double val = Math.pow(((D_NANOS / l) - average) / (double) count, 2);
            sd = sd.add(BigDecimal.valueOf(val));
        }
        return MathUtil.sqrt(sd).doubleValue();
    }

    public ThroughputPerformanceStatistics merge(final ThroughputPerformanceStatistics other)
    {
        return new ThroughputPerformanceStatistics(this.collectionStatistics.merge(other.collectionStatistics), totalInvocations + other.totalInvocations, elapsedTotal.plus(other.elapsedTotal));
    }
}
