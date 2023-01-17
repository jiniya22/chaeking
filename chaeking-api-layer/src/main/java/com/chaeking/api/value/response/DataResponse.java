package com.chaeking.api.value.response;

import lombok.Getter;

@Getter
public class DataResponse<T> extends BaseResponse {
    private T data;

    protected  DataResponse() {
        super();
    }
    private DataResponse(T data) {
        super();
        this.data = data;
    }

    public static <T> DataResponse<T> of(T data) {
        return new DataResponse<>(data);
    }
}
