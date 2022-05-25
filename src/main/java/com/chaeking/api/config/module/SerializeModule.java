package com.chaeking.api.config.module;

import com.chaeking.api.util.DateTimeUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.ValueInstantiators;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SerializeModule {
    public static final SimpleModule dateTimeModule;
    public static final SimpleModule recordNamingStrategyPatchModule;

    static {
        dateTimeModule = new SimpleModule()
                .addSerializer(LocalDate.class, new LocalDateSerializer())
                .addDeserializer(LocalDate.class, new LocalDateDeserializer())
                .addSerializer(LocalTime.class, new LocalTimeSerializer())
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer())
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer())
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        recordNamingStrategyPatchModule = new RecordNamingStrategyPatchModule();
    }

    static class RecordNamingStrategyPatchModule extends SimpleModule {

        @Override
        public void setupModule(SetupContext context) {
            context.addValueInstantiators(new ValueInstantiatorsModifier());
            super.setupModule(context);
        }

        /**
         * Remove when the following issue is resolved:
         * <a href="https://github.com/FasterXML/jackson-databind/issues/2992">Properties naming strategy do not work with Record #2992</a>
         */
        private static class ValueInstantiatorsModifier extends ValueInstantiators.Base {
            @Override
            public ValueInstantiator findValueInstantiator(
                    DeserializationConfig config, BeanDescription beanDesc, ValueInstantiator defaultInstantiator
            ) {
                if (!beanDesc.getBeanClass().isRecord() || !(defaultInstantiator instanceof StdValueInstantiator) || !defaultInstantiator.canCreateFromObjectWith()) {
                    return defaultInstantiator;
                }
                Map<String, BeanPropertyDefinition> map = beanDesc.findProperties().stream().collect(Collectors.toMap(p -> p.getInternalName(), Function.identity()));
                SettableBeanProperty[] renamedConstructorArgs = Arrays.stream(defaultInstantiator.getFromObjectArguments(config))
                        .map(p -> {
                            BeanPropertyDefinition prop = map.get(p.getName());
                            return prop != null ? p.withName(prop.getFullName()) : p;
                        })
                        .toArray(SettableBeanProperty[]::new);

                return new PatchedValueInstantiator((StdValueInstantiator) defaultInstantiator, renamedConstructorArgs);
            }
        }

        private static class PatchedValueInstantiator extends StdValueInstantiator {

            protected PatchedValueInstantiator(StdValueInstantiator src, SettableBeanProperty[] constructorArguments) {
                super(src);
                _constructorArguments = constructorArguments;
            }
        }
    }


    static class LocalDateSerializer extends StdSerializer<LocalDate> {
        public LocalDateSerializer() {
            this(null);
        }
        protected LocalDateSerializer(Class<LocalDate> t) {
            super(t);
        }

        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.format(DateTimeUtils.DATE_FORMATTER));
        }
    }
    static class LocalDateDeserializer extends StdDeserializer<LocalDate> {
        public LocalDateDeserializer() {
            this(null);
        }
        public LocalDateDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return LocalDate.parse(p.getText(), DateTimeUtils.DATE_FORMATTER);
        }
    }

    static class LocalTimeSerializer extends StdSerializer<LocalTime> {
        public LocalTimeSerializer() {
            this(null);
        }
        protected LocalTimeSerializer(Class<LocalTime> t) {
            super(t);
        }

        @Override
        public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.format(DateTimeUtils.TIME_FORMATTER));
        }
    }
    static class LocalTimeDeserializer extends StdDeserializer<LocalTime> {
        public LocalTimeDeserializer() {
            this(null);
        }
        public LocalTimeDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return LocalTime.parse(p.getText(), DateTimeUtils.TIME_FORMATTER);
        }
    }

    static class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
        public LocalDateTimeSerializer() {
            this(null);
        }
        protected LocalDateTimeSerializer(Class<LocalDateTime> t) {
            super(t);
        }

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.format(DateTimeUtils.DATETIME_FORMATTER));
        }
    }
    static class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
        public LocalDateTimeDeserializer() {
            this(null);
        }
        public LocalDateTimeDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return LocalDateTime.parse(p.getText(), DateTimeUtils.DATETIME_FORMATTER);
        }
    }
}
