package com.chaeking.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("datasource")
public class DatasourceConfig {
    private String userName;
    private String password;
}
