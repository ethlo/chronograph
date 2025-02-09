package com.ethlo.time.output.json;

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

import java.util.function.Function;

import com.ethlo.time.ChronographData;
import com.ethlo.time.OutputConfig;
import com.ethlo.time.output.OutputFormatter;

public class JsonOutputFormatter implements OutputFormatter
{
    private final OutputConfig outputConfig;
    private final Function<SerializableChronographData, String> jsonSerializer;

    public JsonOutputFormatter(OutputConfig outputConfig, Function<SerializableChronographData, String> jsonSerializer)
    {
        this.outputConfig = outputConfig;
        this.jsonSerializer = jsonSerializer;
    }

    @Override
    public String format(final ChronographData data)
    {
        final SerializableChronographData serializable = new SerializableChronographData(data, outputConfig);
        return jsonSerializer.apply(serializable);
    }
}
