package samples.usage;

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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.ethlo.chronograph.Chronograph;

class UsageSamples
{
    /**
     * Tasks can be nested. Time spent in a task that is not covered by its subtasks is reported as
     * `<overhead>`.
     */
    @Test
    void hierarchicalTasks()
    {
        final Chronograph c = Chronograph.create();
        c.time("Request", () ->
        {
            for (int i = 0; i < 13; i++)
            {
                c.time("Fetch", () -> busy(12));
            }

            // Overhead
            busy(300);

            c.time("De-serialize", () ->
            {
                // Overhead
                busy(14);
                c.time("JSON de-serialize", () -> busy(44));
            });
        });

        System.out.println(c);
    }

    /**
     * Start and stop a task explicitly. Printing the chronograph renders the results as a table.
     */
    @Test
    void startAndStop()
    {
        final Chronograph chronograph = Chronograph.create();
        chronograph.start("my-task");
        // perform a task
        chronograph.stop();
        System.out.println(chronograph);

        assertEquals(1, chronograph.getTask("my-task").getInvocations());
    }

    /**
     * Time a lambda. The overloads of `time` cover the functional interfaces in `java.util.function`, so
     * a return value is passed through.
     */
    @Test
    void timeLambda()
    {
        final List<Long> myList = new ArrayList<>(Arrays.asList(5L, 3L, 9L, 1L));
        final Chronograph chronograph = Chronograph.create();
        chronograph.time("List sort", () -> myList.sort(Comparator.naturalOrder()));
        final Long max = chronograph.time("Find max", () -> myList.get(myList.size() - 1));
        System.out.println(chronograph);

        assertEquals(9L, max);
    }

    private static void busy(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}
