package samples.themes;

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

import org.junit.jupiter.api.Test;

import com.ethlo.chronograph.Chronograph;
import com.ethlo.chronograph.OutputConfig;
import com.ethlo.chronograph.output.table.AnsiColor;
import com.ethlo.chronograph.output.table.TableOutputFormatter;
import com.ethlo.chronograph.output.table.TableTheme;
import com.ethlo.chronograph.output.table.TableThemes;

class ThemeSamples
{
    /**
     * Build a theme from scratch.
     */
    @Test
    void customTheme()
    {
        final TableTheme myTheme = TableTheme.builder()
                .stringColor(AnsiColor.BLUE)
                .numericColor(AnsiColor.GREEN)
                .horizontalSeparator(" ")
                .verticalSpacerColor(AnsiColor.GRAY)
                .horizontalSpacerColor(AnsiColor.GRAY)
                .build();

        final Chronograph chronograph = Chronograph.create();
        chronograph.time("my-task", () -> Math.sqrt(42));
        System.out.println(new TableOutputFormatter(myTheme, OutputConfig.DEFAULT).format(chronograph.getTaskData()));

        assertEquals(AnsiColor.BLUE, myTheme.getStringColor());
    }

    /**
     * Or start from a predefined theme and change what you need.
     */
    @Test
    void adjustTheme()
    {
        final TableTheme myTheme = TableThemes.ROUNDED.begin()
                .stringColor(AnsiColor.YELLOW)
                .build();

        assertEquals(AnsiColor.YELLOW, myTheme.getStringColor());
    }
}
