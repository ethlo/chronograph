package com.ethlo.time.chronograph.statistics;

/*-
 * #%L
 * Chronograph
 * %%
 * Copyright (C) 2019 - 2024 Morten Haraldsen (ethlo)
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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.ethlo.time.chronograph.internal.util.IndexedCollection;
import com.ethlo.time.chronograph.internal.util.IndexedCollectionStatistics;
import com.ethlo.time.chronograph.internal.util.LongList;

class StatisticsTest
{
    private final IndexedCollection<Long> list = new LongList().addAll(List.of(200L, 400L, 400L, 400L, 500L, 500L, 700L, 900L));
    private final PerformanceStatistics durationStats = new PerformanceStatistics(new IndexedCollectionStatistics(list));

    @Test
    void getAverage()
    {
        assertThat(durationStats.getAverage()).isEqualTo(Duration.ofNanos(500));
    }

    @Test
    void getStandardDeviation()
    {
        assertThat(durationStats.getStandardDeviation()).isEqualTo(Duration.ofNanos(200));
    }

    @Test
    void getMedianNoValuesValue()
    {
        final IndexedCollection<Long> list = new LongList().addAll(List.of());
        final PerformanceStatistics durationStats = new PerformanceStatistics(new IndexedCollectionStatistics(list));
        assertThat(durationStats.getMedian()).isEqualTo(null);
    }
}
