package com.ethlo.time;

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

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.Duration;

public class ReportUtil
{
    public static final int SECONDS_PER_HOUR = 3_600;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int NANOS_PER_MILLI = 1_000_000;
    private static final int NANOS_PER_MICRO = 1_000;
    private static final long MICRO = 1_000_000;
    private static final long MILLI = 1_000;

    public static String humanReadable(Duration duration)
    {
        final long seconds = duration.getSeconds();
        final int hours = (int) seconds / SECONDS_PER_HOUR;
        int remainder = (int) seconds - hours * SECONDS_PER_HOUR;
        final int mins = remainder / SECONDS_PER_MINUTE;
        remainder = remainder - mins * SECONDS_PER_MINUTE;
        final int secs = remainder;

        final long nanos = duration.getNano();
        final int millis = (int) nanos / NANOS_PER_MILLI;
        remainder = (int) nanos - millis * NANOS_PER_MILLI;
        final int micros = remainder / NANOS_PER_MICRO;
        remainder = remainder - micros * NANOS_PER_MICRO;
        final int nano = remainder;

        final NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumIntegerDigits(2);

        final NumberFormat df = NumberFormat.getNumberInstance();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        df.setRoundingMode(RoundingMode.HALF_UP);

        final StringBuilder sb = new StringBuilder();
        if (hours > 0)
        {
            sb.append(nf.format(hours)).append(":");
        }
        if (hours > 0 || mins > 0)
        {
            sb.append(nf.format(mins)).append(":");
        }

        final boolean hasMinuteOrMore = hours > 0 || mins > 0;
        final boolean hasSecondOrMore = hasMinuteOrMore || secs > 0;
        if (hasSecondOrMore && !hasMinuteOrMore)
        {
            final NumberFormat dfSec = NumberFormat.getNumberInstance();
            dfSec.setMinimumFractionDigits(0);
            dfSec.setMaximumFractionDigits(0);
            dfSec.setMinimumIntegerDigits(3);
            dfSec.setMaximumIntegerDigits(3);
            sb.append(seconds).append('.').append(dfSec.format(nanos / (double) NANOS_PER_MILLI)).append(" s");
        }
        else if (hasSecondOrMore)
        {
            sb.append(nf.format(secs)).append(".").append(millis);
        }
        else
        {
            // Sub-second
            if (millis > 0)
            {
                sb.append(df.format(nanos / (double) NANOS_PER_MILLI)).append(" ms ");
            }

            if (millis == 0 && micros > 0)
            {
                sb.append(df.format(nanos / (double) NANOS_PER_MICRO)).append(" μs ");
            }

            if (millis == 0 && micros == 0 && nano > 0)
            {
                sb.append(nano).append(" ns ");
            }
        }

        return sb.toString().trim();
    }

    public static String humanReadable(final double throughput)
    {
        final NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setGroupingUsed(true);

        if (throughput > MICRO)
        {
            nf.setMaximumFractionDigits(0);
        }
        else if (throughput > MILLI)
        {
            nf.setMaximumFractionDigits(1);
        }
        else
        {
            nf.setMaximumFractionDigits(2);
        }

        return nf.format(throughput);
    }

    public static String formatInteger(final long value)
    {
        final NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(true);
        return nf.format(value);
    }
}
