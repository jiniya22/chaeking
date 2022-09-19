package com.chaeking.api.domain.value.data4library;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(value = PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public abstract class BaseData4LibraryResponse {
    private int pageNo;
    private int pageSize;
    private int numFound;
    private int resultNum;
}
