package com.chaeking.api.config;

import com.chaeking.api.model.ChaekingProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import javax.servlet.annotation.MultipartConfig;

@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final String[] RESOURCE_LOCATIONS = {
            "classpath:/static/"
    };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/static/**")
                .addResourceLocations(RESOURCE_LOCATIONS)
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());

        registry.addResourceHandler("/etc/**")
                .addResourceLocations("file:///" + ChaekingProperties.getImageUploadPath())
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080", "http://localhost:8081", "https://www.chaeking.com", "https://chaeking.com")
                .allowedMethods("GET", "OPTIONS", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("Access-Control-Allow-Origin", "X-Requested-With", "Origin", "Content-Type", "Accept",
                        "Authorization", "X-Chaeking-User-Id", "X-Error-Code")
                ;
    }

}
