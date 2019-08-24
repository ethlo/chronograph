package com.ethlo.time;

import java.time.Duration;

import com.ethlo.time.statistics.PerformanceStatistics;

public class TaskPerformanceStatistics
{
    private final String name;
    private final PerformanceStatistics<Duration> durationStatistics;
    private final PerformanceStatistics<Double> throughputStatistics;
    private final long sampleSize;

    public TaskPerformanceStatistics(String name, final long sampleSize, final PerformanceStatistics<Duration> durationStatistics, final PerformanceStatistics<Double> throughputStatistics)
    {
        this.name = name;
        this.sampleSize = sampleSize;
        this.durationStatistics = durationStatistics;
        this.throughputStatistics = throughputStatistics;
    }

    public PerformanceStatistics<Duration> getDurationStatistics()
    {
        return durationStatistics;
    }

    public PerformanceStatistics<Double> getThroughputStatistics()
    {
        return throughputStatistics;
    }

    public String getName()
    {
        return name;
    }

    public long getSampleSize()
    {
        return sampleSize;
    }
}
