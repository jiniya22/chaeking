package com.chaeking.api.config.vault;

import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("chaeking")
public class ChaekingConfig {
    @Getter private static String secret;

    public void setSecret(String secret) { if (Strings.isBlank(secret)) ChaekingConfig.secret = secret; }
}
