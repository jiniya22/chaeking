package com.chaeking.api.config;

import com.chaeking.api.config.module.SerializeModule;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public final class WebConfig {

    @Bean
    public ObjectMapper jsonMapper() {
        return JsonMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                .disable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .addModule(SerializeModule.dateTimeModule)
                .addModule(SerializeModule.recordNamingStrategyPatchModule)
                .build();
    }

    @Bean
    public ModelResolver modelResolver(@Qualifier("jsonMapper") ObjectMapper jsonMapper) {
        ModelResolver m = new ModelResolver(jsonMapper);
        return m;
    }

}
