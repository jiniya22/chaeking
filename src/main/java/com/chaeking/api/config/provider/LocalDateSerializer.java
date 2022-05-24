package com.chaeking.api.config.provider;

import com.chaeking.api.util.DateTimeUtils;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateSerializer extends StdDeserializer<LocalDate> {
    public LocalDateSerializer() {
        this(null);
    }
    public LocalDateSerializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        return LocalDate.parse(p.getText(), DateTimeUtils.DATE_FORMATTER);
    }
}
//public class LocalDateSerializer extends JsonSerializer<LocalDate> {
//    @Override
//    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//
//    }
//}