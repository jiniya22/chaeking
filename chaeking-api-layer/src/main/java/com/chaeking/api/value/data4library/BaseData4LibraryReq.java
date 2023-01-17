package com.chaeking.api.value.data4library;

import com.chaeking.api.config.vault.ChaekingConfig;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public abstract class BaseData4LibraryReq {
    @Hidden private String authKey;
    @Hidden private String format;

    public BaseData4LibraryReq() {
        this.authKey = ChaekingConfig.Data4library.getAuthKey();
        this.format = "json";
    }
}
