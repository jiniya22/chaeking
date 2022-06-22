package com.chaeking.api.domain.value;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChaekingProperties {
    static String url;

    @Value("${chaeking.url}")
    public void setUrl(String url) {
        if(ChaekingProperties.url == null)
            ChaekingProperties.url = url;
    }

    public static String getUrl() {
        return ChaekingProperties.url;
    }
}
