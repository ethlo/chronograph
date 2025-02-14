package com.ethlo.chronograph.output;

/*-
 * #%L
 * Chronograph
 * %%
 * Copyright (C) 2019 - 2024 Morten Haraldsen (ethlo)
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

import com.ethlo.chronograph.ChronographData;
import com.ethlo.chronograph.output.json.JsonOutputFormatter;
import com.ethlo.chronograph.output.table.TableOutputFormatter;

/**
 * Formatter interface for formatting @{@link ChronographData}
 *
 * @see TableOutputFormatter
 * @see JsonOutputFormatter
 */
@FunctionalInterface
public interface OutputFormatter
{
    /**
     * Format the data according to the implementation
     *
     * @param data The data to format
     * @return The formatted data as a String
     */
    String format(ChronographData data);
}
