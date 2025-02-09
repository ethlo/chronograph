package com.ethlo.time;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.DecimalFormat;
import java.time.Duration;

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

    static
    {
        final SimpleModule module = new SimpleModule();
        module.addSerializer(Duration.class, new PlainDurationSerializer());
        mapper.registerModule(module);

        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public static String asciiTable(Chronograph chronograph)
    {
        return formatter.format(chronograph.getTaskData());
    }

    public static String json(Chronograph chronograph, OutputConfig outputConfig)
    {
        final SerializableChronographData serializable = new SerializableChronographData(chronograph.getTaskData(), outputConfig);
        try
        {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(serializable);
        }
        catch (JsonProcessingException e)
        {
            throw new UncheckedIOException(e);
        }
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
