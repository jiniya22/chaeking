package com.chaeking.api.model.data4library;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public abstract class BaseData4LibraryPagingReq extends BaseData4LibraryReq {
    @Setter private int pageNo;
    @Setter private int pageSize;

    public BaseData4LibraryPagingReq() {
        super();
        this.pageNo = 1;
        this.pageSize = 10;
    }
}
