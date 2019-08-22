package com.ethlo.time;

import java.time.Duration;

public class RateLimitedTaskInfo extends TaskInfo
{
    private final long nanosInterval;
    private long lastStopped;
    private boolean recording = true;

    RateLimitedTaskInfo(final String name, Duration sampleinterval)
    {
        super(name);
        this.nanosInterval = sampleinterval.toNanos();
    }

    @Override
    long start()
    {
        return super.start();
    }

    @Override
    void stopped(final long ts, final boolean ignoreState)
    {
        super.stopped(ts, ignoreState);
        lastStopped = ts;
    }
}
