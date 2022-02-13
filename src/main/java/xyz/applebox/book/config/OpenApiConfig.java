package xyz.applebox.book.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI(@Value("${book.version}") String appVersion,
                           @Value("${book.url}") String url, @Value("${spring.profiles.active}") String active) {
        Info info = new Info().title("Book API - " + active).version(appVersion)
                .description("Book API입니다.")
                .contact(new Contact().name("jini").url("https://blog.jiniworld.me/").email("jini@jiniworld.me"))
                .license(new License().name("MIT License").url("https://github.com/jiniya22/book/blob/master/LICENSE"));

        List<Server> servers = Arrays.asList(new Server().url(url).description("book (" + active +")"));

//        SecurityScheme securityScheme = new SecurityScheme()
//                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
//                .in(SecurityScheme.In.HEADER).name("Authorization");
//        SecurityRequirement schemaRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
//                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
//                .addSecurityItem(schemaRequirement)
//                .security(Arrays.asList(schemaRequirement))
                .info(info)
                .servers(servers);
    }
}
