package samples.sampling;

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

import java.time.Duration;

import org.junit.jupiter.api.Test;

import com.ethlo.chronograph.CaptureConfig;
import com.ethlo.chronograph.Chronograph;

class SamplingSamples
{
    /**
     * Only capture an invocation when at least `minInterval` has passed since the last captured one. All
     * invocations are still counted.
     */
    @Test
    void minInterval()
    {
        final Chronograph chronograph = Chronograph
                .create(CaptureConfig
                        .minInterval(Duration.ofNanos(10_000))); // 10 microseconds

        for (int i = 0; i < 100_000; i++)
        {
            chronograph.time("quick-task", () -> Math.sqrt(42));
        }

        assertEquals(100_000, chronograph.getTask("quick-task").getInvocations());
    }
}
