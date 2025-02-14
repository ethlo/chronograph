package com.ethlo.time.chronograph.internal.ascii;

/*-
 * #%L
 * chronograph
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
import java.text.NumberFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class ReportUtil
{
    private static final int NANOS_PER_MICRO = 1_000;
    private static final long NANOS_PER_MILLI = 1_000_000;
    private static final long NANOS_PER_SECOND = 1_000_000_000;


    public static String humanReadable(Duration duration)
    {
        return humanReadable(duration, getSummaryResolution(duration));
    }

    public static String humanReadable(Duration duration, ChronoUnit unit)
    {
        switch (unit)
        {
            case NANOS:
                return duration.toNanos() + " ns";
            case MICROS:
                return BigDecimal.valueOf(duration.toNanos()).divide(BigDecimal.valueOf(NANOS_PER_MICRO), 2, RoundingMode.HALF_UP) + " us";
            case MILLIS:
                return BigDecimal.valueOf(duration.toNanos()).divide(BigDecimal.valueOf(NANOS_PER_MILLI), 2, RoundingMode.HALF_UP) + " ms";
            case SECONDS:
                return BigDecimal.valueOf(duration.toNanos()).divide(BigDecimal.valueOf(NANOS_PER_SECOND), 3, RoundingMode.HALF_UP) + " s";
            case MINUTES:
            case HOURS:
            default:
                // Use dynamic HH:mm:ss format
                long seconds = duration.getSeconds();
                long hours = seconds / 3600;
                long minutes = (seconds % 3600) / 60;
                long remainingSeconds = seconds % 60;

                if (hours > 0)
                {
                    return String.format("%d:%02d:%02d", hours, minutes, remainingSeconds);
                }
                else
                {
                    return String.format("%02d:%02d", minutes, remainingSeconds);
                }
        }
    }

    public static String formatInteger(final long value)
    {
        final NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(true);
        return nf.format(value);
    }

    public static ChronoUnit getSummaryResolution(final Duration totalExecutionTime)
    {
        if (totalExecutionTime == null || totalExecutionTime.isNegative() || totalExecutionTime.isZero())
        {
            return ChronoUnit.NANOS;
        }

        final long nanos = totalExecutionTime.toNanos();

        // Define thresholds for units
        if (nanos < 1_000)
        { // Less than 1 microsecond
            return ChronoUnit.NANOS;
        }
        else if (nanos < 1_000_000)
        { // Less than 1 millisecond
            return ChronoUnit.MICROS;
        }
        else if (nanos < 1_000_000_000)
        { // Less than 1 second
            return ChronoUnit.MILLIS;
        }
        else if (nanos < 60L * 1_000_000_000)
        { // Less than 1 minute
            return ChronoUnit.SECONDS;
        }
        else if (nanos < 3600L * 1_000_000_000)
        { // Less than 1 hour
            return ChronoUnit.MINUTES;
        }
        return ChronoUnit.HOURS;
    }
}
