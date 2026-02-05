package com.ethlo.chronograph;

/*-
 * #%L
 * Chronograph
 * %%
 * Copyright (C) 2019 - 2026 Morten Haraldsen (ethlo)
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

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChronographTotalTimeTest
{

    @Test
    @DisplayName("Total time should not double-count nested tasks")
    void totalTimeShouldNotDoubleCountNestedTasks() throws InterruptedException
    {
        final Chronograph chronograph = Chronograph.create("Double Count Test");

        // Parent task takes ~100ms total
        chronograph.time("parent", () ->
                {
                    delay(50);
                    chronograph.time("child", () -> delay(50));
                }
        );
        final Duration total = chronograph.getTotalTime();

        assertThat(total.toMillis())
                .as("Total time should be roughly the duration of the parent task")
                .isLessThan(105);
    }

    @Test
    @DisplayName("Total time in ChronographData should match root tasks sum")
    void totalTimeInDataShouldMatchRootTasks()
    {
        Chronograph chronograph = Chronograph.create();

        chronograph.time("task1", () -> delay(20));
        chronograph.time("task2", () -> delay(30));
        ChronographData data = chronograph.getTaskData();

        // Verify root tasks logic
        long rootSum = data.getRootTasks().stream()
                .mapToLong(t -> t.getTime().toNanos())
                .sum();

        assertThat(data.getTotalTime().toNanos()).isEqualTo(rootSum);
        assertThat(data.getTotalTime().toMillis()).isCloseTo(50L, Offset.offset(2L));
    }

    @Test
    @DisplayName("Empty chronograph should return zero duration")
    void emptyChronographShouldReturnZeroDuration()
    {
        Chronograph chronograph = Chronograph.create();
        assertThat(chronograph.getTotalTime()).isEqualTo(Duration.ZERO);
        assertThat(chronograph.getTaskData().getTotalTime()).isEqualTo(Duration.ZERO);
    }

    private void delay(int i)
    {
        try
        {
            Thread.sleep(i);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}
