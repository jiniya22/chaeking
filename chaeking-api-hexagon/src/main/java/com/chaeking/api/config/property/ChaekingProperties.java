package com.chaeking.api.config.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChaekingProperties {
    static String url;
    static String imageUploadPath;
    static String imageUrlPrefix;

    @Value("${chaeking.url}")
    public void setUrl(String url) {
        if(ChaekingProperties.url == null)
            ChaekingProperties.url = url;
    }

    @Value("${chaeking.image.upload-path}")
    public void setImageUploadPath(String imageUploadPath) {
        if(ChaekingProperties.imageUploadPath == null)
            ChaekingProperties.imageUploadPath = imageUploadPath  + "/";
    }

    @Value("${chaeking.image.url}")
    public void setImageUrlPrefix(String imageUrlPrefix) {
        if(ChaekingProperties.imageUrlPrefix == null)
            ChaekingProperties.imageUrlPrefix = imageUrlPrefix + "/";
    }

    public static String getUrl() {
        return ChaekingProperties.url;
    }

    public static String getImageUploadPath() {
        return ChaekingProperties.imageUploadPath;
    }

    public static String getImageUrlPrefix() {
        return ChaekingProperties.imageUrlPrefix;
    }
}