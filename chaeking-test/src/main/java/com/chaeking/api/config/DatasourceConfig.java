package com.chaeking.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("spring.datasource")
public class DatasourceConfig {
    private String username;
    private String password;
}
