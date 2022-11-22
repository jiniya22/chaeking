package com.chaeking.api.config.vault;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("chaeking")
public class ChaekingConfig {
    @Getter private static String secret;
    private static Data4library data4library;

    public static class Data4library {
        @Getter private static String authKey;

        public void setAuthKey(String authKey) { Data4library.authKey = authKey; }
    }

    public void setSecret(String secret) { if (ChaekingConfig.secret == null) ChaekingConfig.secret = secret; }
    public void setData4library(Data4library library) { if(ChaekingConfig.data4library == null) ChaekingConfig.data4library = library; }
}
