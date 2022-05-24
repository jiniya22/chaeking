package com.chaeking.api.config;

import com.chaeking.api.config.provider.LocalDateSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.core.jackson.ModelResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public final class WebConfig {

    @Bean
    public ObjectMapper objectMapper() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(LocalDate.class, new LocalDateSerializer());

        return new ObjectMapper()
//                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .registerModules(simpleModule);
    }
    @Bean
    public ModelResolver modelResolver(@Qualifier("objectMapper") ObjectMapper objectMapper) {
        ModelResolver m = new ModelResolver(objectMapper);
        return m;
    }

//    @Bean
//    public ModelResolver modelResolver(ObjectMapper objectMapper) {
//        return new ModelResolver(objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE));
//    }

}
