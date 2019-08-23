package com.ethlo.time;

import java.time.Duration;

public class RateLimitedTaskInfo extends TaskInfo
{
    private final long nanosInterval;
    private long lastStopped;

    RateLimitedTaskInfo(final String name, Duration sampleinterval)
    {
        super(name);
        this.nanosInterval = sampleinterval.toNanos();
    }

    @Override
    boolean stopped(final long ts, final boolean ignoreState)
    {
        super.stopped(ts, ignoreState);
        final long elapsed = ts - lastStopped;
        if (lastStopped == 0 || elapsed > nanosInterval)
        {
            lastStopped = ts;
            return true;
        }
        return false;
    }
}
