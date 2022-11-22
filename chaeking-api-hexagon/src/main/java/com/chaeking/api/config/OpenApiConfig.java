package com.chaeking.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
class OpenApiConfig {
    @Bean
    public OpenAPI openAPI(@Value("${chaeking.url}") String url, @Value("${spring.profiles.active}") String active) {
        Info info = new Info().title("Chaeking API - " + active)
                .description("Hexagonal Architecture를 이용한 책킹 API")
                .contact(new Contact().name("jini").url("https://blog.jiniworld.me/").email("jini@chaeking.com"))
                .license(new License().name("MIT License").url("https://github.com/jiniya22/chaeking-api/blob/master/LICENSE"));

        List<Server> servers = Arrays.asList(new Server().url(url).description("chaeking-api (" + active +")"));

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("access_token",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER).name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList("access_token"))
                .info(info)
                .servers(servers);
    }
}
