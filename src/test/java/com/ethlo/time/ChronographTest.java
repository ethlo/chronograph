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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.time.Duration;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChronographTest
{
    private static final Logger logger = LoggerFactory.getLogger(ChronographTest.class);

    private final String taskName = "a long task name";

    @Test
    public void startStopSequenceSingleTask()
    {
        final Chronograph chronograph = Chronograph.create();

        for (int i = 1; i <= 1_000; i++)
        {
            chronograph.start(taskName);
            millisecondTask();
            chronograph.stop(taskName);
        }

        logger.info(chronograph.prettyPrint());
    }

    @Test
    public void startStopSequenceMultipleTasks() throws InterruptedException
    {
        final Chronograph chronograph = Chronograph.create();

        final String taskName2 = "bar";
        final String taskName3 = "baz baz baz baz baz baz";

        for (int i = 1; i <= 100_000; i++)
        {
            chronograph.start(taskName);
            microsecondTask();
            chronograph.stop(taskName);

            chronograph.start(taskName2);
            microsecondTask();
            chronograph.stop(taskName2);

            chronograph.start(taskName3);
            microsecondTask();
            chronograph.stop(taskName3);
        }

        logger.info(chronograph.prettyPrint());
    }

    private void millisecondTask()
    {
        try
        {
            SleepUtil.sleepNanos(1_000_000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void microsecondTask()
    {
        try
        {
            SleepUtil.sleepNanos(1_000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void stopBeforeStart()
    {
        final Chronograph chronograph = Chronograph.create();
        chronograph.stop(taskName);
        fail("Should throw");
    }

    @Test(expected = IllegalStateException.class)
    public void stopAfterStopped()
    {
        final Chronograph chronograph = Chronograph.create();
        chronograph.start(taskName);
        chronograph.stop(taskName);
        chronograph.stop(taskName);
        fail("Should throw");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullTask()
    {
        final Chronograph chronograph = Chronograph.create();
        chronograph.start(null);
        fail("Should throw");
    }

    @Test(expected = IllegalStateException.class)
    public void sequentialStartk()
    {
        final Chronograph chronograph = Chronograph.create();
        chronograph.start(taskName);
        chronograph.start(taskName);
    }

    @Test
    public void testGranularity()
    {
        final Chronograph chronograph = Chronograph.create();
        for (int i = 0; i < 1_000_000; i++)
        {
            chronograph.start(taskName);
            chronograph.stop(taskName);
        }
        logger.info("Granularity: {}", chronograph.getTaskInfo(taskName).getAverageTaskTime().getNano() + " nanos");
    }

    @Test
    public void resetAll()
    {
        final Chronograph chronograph = Chronograph.create();
        chronograph.start(taskName);
        chronograph.stop(taskName);
        chronograph.getElapsedTime(taskName);
        assertThat(chronograph.getTaskNames()).containsExactly(taskName);
        chronograph.resetAll();
        assertThat(chronograph.getTaskNames()).isEmpty();
    }

    @Test
    public void testIsRunning()
    {
        final Chronograph chronograph = Chronograph.create();
        assertThat(chronograph.isRunning(taskName)).isFalse();
        chronograph.start(taskName);
        assertThat(chronograph.isRunning(taskName)).isTrue();
        chronograph.stop(taskName);
        assertThat(chronograph.isRunning(taskName)).isFalse();
    }

    @Test
    public void getTaskInfo()
    {
        final Chronograph chronograph = Chronograph.create();
        chronograph.start("a");
        chronograph.start("b");
        chronograph.start("c");
        chronograph.stop();
        assertThat(chronograph.getTaskInfo()).hasSize(3);
    }

    @Test
    public void getTotalTaskTimeForEmpty()
    {
        final Chronograph chronograph = Chronograph.create();
        assertThat(chronograph.getTotalTime()).isEqualTo(Duration.ZERO);
        chronograph.start(taskName);
        assertThat(chronograph.getTotalTime()).isEqualTo(Duration.ZERO);
        chronograph.stop();
        assertThat(chronograph.getTotalTime()).isNotEqualTo(Duration.ZERO);
    }

    @Test
    public void testIsAnyRunning()
    {
        final Chronograph chronograph = Chronograph.create();
        chronograph.start(taskName);
        assertThat(chronograph.isAnyRunning()).isTrue();
        chronograph.stop();
        assertThat(chronograph.isAnyRunning()).isFalse();
    }

    @Test
    public void testGetAverageForNonStoppedTask()
    {
        final Chronograph chronograph = Chronograph.create();
        chronograph.start(taskName);
        assertThat(chronograph.getTaskInfo(taskName).getAverageTaskTime()).isEqualTo(Duration.ZERO);
    }
}
