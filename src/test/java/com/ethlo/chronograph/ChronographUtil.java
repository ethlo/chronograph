package com.ethlo.chronograph;

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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.DecimalFormat;
import java.time.Duration;

import com.ethlo.chronograph.output.table.TableOutputFormatter;
import com.ethlo.chronograph.output.json.JsonOutputFormatter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public abstract class ChronographUtil
{
    private static final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
            .findAndRegisterModules();
    private static final TableOutputFormatter formatter = new TableOutputFormatter();
    private static final JsonOutputFormatter jsonOutputFormatter;

    static
    {
        final SimpleModule module = new SimpleModule();
        module.addSerializer(Duration.class, new PlainDurationSerializer());
        mapper.registerModule(module);

        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        jsonOutputFormatter = new JsonOutputFormatter(OutputConfig.EXTENDED.percentiles(75, 90, 95, 99, 99.9), data ->
        {
            try
            {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            }
            catch (JsonProcessingException e)
            {
                throw new UncheckedIOException(e);
            }
        });
    }

    public static String table(Chronograph chronograph)
    {
        return formatter.format(chronograph.getTaskData());
    }

    public static String json(Chronograph chronograph)
    {
        return jsonOutputFormatter.format(chronograph.getTaskData());
    }

    static class PlainDurationSerializer extends JsonSerializer<Duration>
    {
        private static final ThreadLocal<DecimalFormat> threadLocalDecimalFormat =
                ThreadLocal.withInitial(() -> new DecimalFormat("0.################"));

        @Override
        public void serialize(Duration value, JsonGenerator gen, SerializerProvider serializers) throws IOException
        {
            if (value != null)
            {
                gen.writeString(threadLocalDecimalFormat.get().format(value.toNanos() / 1_000_000_000D));
            }
        }
    }
}
