package com.ethlo.time;

import java.time.Duration;
import java.util.List;

public class ChronographData
{
    private final String name;
    private final List<TaskPerformanceStatistics> taskStatistics;
    private final Duration totalTime;

    public ChronographData(final String name, final List<TaskPerformanceStatistics> taskStatistics, final Duration totalTime)
    {
        this.name = name;
        this.taskStatistics = taskStatistics;
        this.totalTime = totalTime;
    }

    public String getName()
    {
        return name;
    }

    public List<TaskPerformanceStatistics> getTaskStatistics()
    {
        return taskStatistics;
    }

    public Duration getTotalTime()
    {
        return totalTime;
    }

    public boolean isEmpty()
    {
        return this.taskStatistics.isEmpty();
    }
}
