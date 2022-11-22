package com.chaeking.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
//@EnableConfigurationProperties({BookSearchConfig.class, ChaekingConfig.class})
@EnableJpaAuditing
class PersistenceAdapterConfig {
}
