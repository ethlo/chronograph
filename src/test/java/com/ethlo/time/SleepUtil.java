package com.ethlo.time;

import java.util.concurrent.TimeUnit;

public class SleepUtil
{
    private static final long SLEEP_PRECISION = TimeUnit.MILLISECONDS.toNanos(2);
    private static final long SPIN_YIELD_PRECISION = TimeUnit.MILLISECONDS.toNanos(2);

    private SleepUtil()
    {
        // No instances of this class
    }

    public static void sleepNanos(long nanoDuration) throws InterruptedException
    {
        final long end = System.nanoTime() + nanoDuration;
        long timeLeft = nanoDuration;
        do
        {
            if (timeLeft > SLEEP_PRECISION)
            {
                Thread.sleep(1);
            }
            else
            {
                if (timeLeft > SPIN_YIELD_PRECISION)
                {
                    Thread.yield();
                }
            }
            timeLeft = end - System.nanoTime();

            if (Thread.interrupted())
                throw new InterruptedException();
        } while (timeLeft > 0);
    }
}
