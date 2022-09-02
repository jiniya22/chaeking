package com.chaeking.api.domain.value.data4library;

import com.chaeking.api.config.vault.ChaekingConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class BaseData4LibraryReq {
    private String authKey;
    private String format;
    @Setter private int pageNo;
    @Setter private int pageSize;

    public BaseData4LibraryReq() {
        this.authKey = ChaekingConfig.Data4library.getAuthKey();
        this.format = "json";
        this.pageNo = 1;
        this.pageSize = 10;
    }
}
