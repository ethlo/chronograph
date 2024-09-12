package com.ethlo.time.statistics;

import com.ethlo.util.IndexedCollection;
import com.ethlo.util.IndexedCollectionStatistics;

import com.ethlo.util.LongList;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ThroughputPerformanceStatisticsTest
{

    @Test
    void getStandardDeviation()
    {
        final IndexedCollection<Long> list = new LongList(1000);
        list.add(123);
        final ThroughputPerformanceStatistics stats = new ThroughputPerformanceStatistics(new IndexedCollectionStatistics(list), 5, Duration.ofMillis(456));
        stats.getStandardDeviation();
    }
}