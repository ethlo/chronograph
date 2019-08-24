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

import java.time.Duration;

import com.ethlo.util.IndexedCollectionStatistics;

public abstract class PerformanceStatistics<T>
{
    protected final IndexedCollectionStatistics collectionStatistics;
    protected final long totalInvocations;
    protected final Duration elapsedTotal;

    public PerformanceStatistics(final IndexedCollectionStatistics collectionStatistics, long totalInvocations, Duration elapsedTotal)
    {
        this.collectionStatistics = collectionStatistics;
        this.totalInvocations = totalInvocations;
        this.elapsedTotal = elapsedTotal;
    }

    public long getTotalInvocations()
    {
        return totalInvocations;
    }

    public Duration getElapsedTotal()
    {
        return elapsedTotal;
    }

    public abstract T getAverage();

    public abstract T getMedian();

    public abstract T getPercentile(double limit);

    public abstract T getMin();

    public abstract T getMax();

    public abstract T getStandardDeviation();
}
