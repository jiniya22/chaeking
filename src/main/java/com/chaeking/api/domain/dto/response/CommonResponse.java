package com.chaeking.api.domain.dto.response;

import lombok.Getter;

@Getter
public class CommonResponse<T> extends BaseResponse {
    private T data;

    public CommonResponse() {
        super();
    }
    public CommonResponse(T data) {
        this();
        this.data = data;
    }
}
