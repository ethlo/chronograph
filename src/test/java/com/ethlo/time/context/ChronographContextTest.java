package com.ethlo.time.context;

/*-
 * #%L
 * Chronograph
 * %%
 * Copyright (C) 2019 - 2025 Morten Haraldsen (ethlo)
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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ethlo.ascii.TableThemes;
import com.ethlo.time.CaptureConfig;
import com.ethlo.time.Chronograph;
import com.ethlo.time.ChronographData;
import com.ethlo.time.OutputConfig;
import com.ethlo.time.TableOutputformatter;

class ChronographContextTest
{
    private final ChronographContext chronographContext = new ChronographContext();

    @BeforeEach
    void setUp()
    {
        // Set up any necessary configurations or defaults if needed
        chronographContext.setCaptureConfig(CaptureConfig.DEFAULT);
        chronographContext.setOutputConfig(OutputConfig.DEFAULT);
        chronographContext.setTheme(TableThemes.ASCII);
    }

    @Test
    void testGetAllWithMultipleThreads()
    {
        int threadCount = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // Simulate multiple threads calling get()
        for (int i = 0; i < threadCount; i++)
        {
            executorService.submit(() ->
            {
                chronographContext.get().time("Task", () ->
                {
                    busy();
                    chronographContext.get().time("SubTask", this::busy);
                });

                System.out.println(chronographContext.prettyPrint());
            });
        }

        // Wait for all threads to finish
        executorService.shutdown();
        while (!executorService.isTerminated())
        {
            // Ensure all threads have completed
        }

        // Verify that ChronographContext.getAll() contains all instances created by the threads
        final List<Chronograph> allChronographs = chronographContext.getAll();

        // Assert that the list contains 3 Chronograph instances (one per thread)
        assertThat(allChronographs).hasSize(threadCount);

        final ChronographData merged = chronographContext.merged(allChronographs);
        System.out.println(new TableOutputformatter().format(merged));
    }

    private void busy()
    {
        try
        {
            Thread.sleep(ThreadLocalRandom.current().nextInt(100));
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().notify();
        }
    }
}
