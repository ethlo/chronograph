package com.ethlo.util;

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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import com.ethlo.chronograph.internal.ascii.ReportUtil;

public class DurationUtilTest
{
    @Test
    void humanReadableFormatMoreThanHour()
    {
        assertThat(ReportUtil.humanReadable(Duration.ofSeconds(4712).withNanos(123456789))).isEqualTo("1:18:32");
    }

    @Test
    void humanReadableFormatLessThanHour()
    {
        assertThat(ReportUtil.humanReadable(Duration.ofSeconds(2000).withNanos(123456789))).isEqualTo("33:20");
    }

    @Test
    void humanReadableFormatLessThanMinute()
    {
        assertThat(ReportUtil.humanReadable(Duration.ofSeconds(8).withNanos(125_956_789))).isEqualTo("8.126 s");
    }

    @Test
    void humanReadableFormatLessThanMinute2()
    {
        assertThat(ReportUtil.humanReadable(Duration.ofSeconds(8).withNanos(1_000_000))).isEqualTo("8.001 s");
    }

    @Test
    void humanReadableFormatLessThanMinute3()
    {
        assertThat(ReportUtil.humanReadable(Duration.ofSeconds(8))).isEqualTo("8.000 s");
    }

    @Test
    void humanReadableFormatLessThanSecond()
    {
        assertThat(ReportUtil.humanReadable(Duration.ofNanos(123_456_789))).isEqualTo("123.46 ms");
    }

    @Test
    void humanReadableFormatLessThanMillisecond()
    {
        assertThat(ReportUtil.humanReadable(Duration.ofNanos(456_789))).isEqualTo("456.79 us");
    }

    @Test
    void humanReadableFormatLessThanMicrosecond()
    {
        assertThat(ReportUtil.humanReadable(Duration.ofNanos(489))).isEqualTo("489 ns");
    }
}
