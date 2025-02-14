package com.ethlo.time.chronograph.context;

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

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ethlo.time.chronograph.CaptureConfig;
import com.ethlo.time.chronograph.Chronograph;
import com.ethlo.time.chronograph.ChronographData;
import com.ethlo.time.chronograph.OutputConfig;
import com.ethlo.time.chronograph.output.table.TableOutputFormatter;

class ChronographContextTest
{
    private final ChronographContext chronographContext = new ChronographContext();

    @BeforeEach
    void setUp()
    {
        // Set up any necessary configurations or defaults if needed
        assertThat(chronographContext
                .setCaptureConfig(CaptureConfig.DEFAULT)
                .setOutputConfig(OutputConfig.DEFAULT)).isSameAs(chronographContext);
    }

    @Test
    void testGetReturnsSameInstanceForSameThread()
    {
        Chronograph first = chronographContext.get();
        Chronograph second = chronographContext.get();
        assertThat(first).isSameAs(second);
    }

    @Test
    void testGetReturnsDifferentInstanceForDifferentThreads() throws InterruptedException
    {
        Chronograph[] otherThreadInstance = new Chronograph[1];

        Thread thread = new Thread(() -> otherThreadInstance[0] = chronographContext.get());
        thread.start();
        thread.join();

        assertThat(otherThreadInstance[0]).isNotNull().isNotSameAs(chronographContext.get());
    }

    @Test
    void testRemoveDeletesInstanceForCurrentThread()
    {
        Chronograph instance = chronographContext.get();
        chronographContext.remove();
        assertThat(chronographContext.get()).isNotSameAs(instance);
    }

    @Test
    void testSetAndGetOutputConfig()
    {
        OutputConfig config = new OutputConfig();
        chronographContext.setOutputConfig(config);
        assertThat(chronographContext.getOutputConfig()).isSameAs(config);
    }

    @Test
    void testSetAndGetCaptureConfig()
    {
        CaptureConfig config = CaptureConfig.minInterval(Duration.ofNanos(100_000));
        chronographContext.setCaptureConfig(config);
        assertThat(chronographContext.getCaptureConfig()).isSameAs(config);
    }

    @Test
    void testGetAllReturnsAllInstances()
    {
        Chronograph instance1 = chronographContext.get();

        Chronograph[] instance2 = new Chronograph[1];
        Thread thread = new Thread(() -> instance2[0] = chronographContext.get());
        thread.start();
        try
        {
            thread.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        List<Chronograph> allInstances = chronographContext.getAll();
        assertThat(allInstances).containsExactlyInAnyOrder(instance1, instance2[0]);
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
            });
        }

        // Wait for all threads to finish
        executorService.shutdown();
        while (!executorService.isTerminated())
        {
            // Ensure all threads have completed
        }

        // Verify that ChronographchronographContext.getAll() contains all instances created by the threads
        final List<Chronograph> allChronographs = chronographContext.getAll();

        // Assert that the list contains 3 Chronograph instances (one per thread)
        assertThat(allChronographs).hasSize(threadCount);

        final ChronographData merged = ChronographData.merge(allChronographs);
        System.out.println(new TableOutputFormatter().format(merged));
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
