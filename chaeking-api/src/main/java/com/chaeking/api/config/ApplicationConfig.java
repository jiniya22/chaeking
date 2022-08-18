package com.chaeking.api.config;

import com.chaeking.api.config.vault.BookSearchConfig;
import com.chaeking.api.config.vault.ChaekingConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableConfigurationProperties({BookSearchConfig.class, ChaekingConfig.class})
@EnableJpaAuditing
public class ApplicationConfig {
}
