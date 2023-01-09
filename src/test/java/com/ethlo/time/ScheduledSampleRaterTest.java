package com.ethlo.time;
/*-
 * #%L
 * stopwatch
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

import java.math.BigInteger;
import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ethlo.time.statistics.SampleRater;

class SampleRaterTest
{
    private static final Logger logger = LoggerFactory.getLogger(SampleRaterTest.class);

    @Test
    void test()
    {
        final SampleRater<BigInteger> taskPerformance = new SampleRater<>(Duration.ofMillis(500));
        BigInteger sum = BigInteger.valueOf(0);
        for (long i = 0; i < 200_000_000L; i++)
        {
            sum = sum.add(BigInteger.valueOf(i));
            taskPerformance.progress(sum, System.err::println);
        }
        System.out.println(sum);
    }
}
