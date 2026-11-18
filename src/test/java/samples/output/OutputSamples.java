package samples.output;

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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.ethlo.chronograph.Chronograph;
import com.ethlo.chronograph.OutputConfig;
import com.ethlo.chronograph.output.table.TableOutputFormatter;
import com.ethlo.chronograph.output.table.TableThemes;

class OutputSamples
{
    /**
     * Begin from scratch and enable only the columns you want.
     */
    @Test
    void outputConfigFromScratch()
    {
        final OutputConfig outputConfig = new OutputConfig()
                .median(true)
                .standardDeviation(true);

        assertTrue(outputConfig.median());
    }

    /**
     * Begin from one of the predefined configurations, `DEFAULT`, `COMPACT` or `EXTENDED`, and adjust it. Each
     * call returns a new configuration.
     */
    @Test
    void outputConfigFromDefault()
    {
        final OutputConfig outputConfig = OutputConfig.DEFAULT
                .percentiles(75, 90, 99, 99.9, 99.99);

        assertEquals(5, outputConfig.percentiles().length);
    }

    /**
     * Render the results as a table with a theme from `TableThemes` and your output configuration.
     */
    @Test
    void formatAsTable()
    {
        final Chronograph chronograph = Chronograph.create();
        chronograph.time("my-task", () -> Math.sqrt(42));

        final OutputConfig outputConfig = OutputConfig.EXTENDED;
        final String table = new TableOutputFormatter(TableThemes.RED_HERRING, outputConfig)
                .format(chronograph.getTaskData());
        System.out.println(table);

        assertTrue(table.contains("my-task"));
    }
}
