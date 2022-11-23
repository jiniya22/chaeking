package com.chaeking.api.common;

import lombok.Getter;

@Getter
public class DataResponse<T> {
    private final String result;
    private final String reason;
    private final T data;

    private DataResponse(T data) {
        this.reason = "success";
        this.result = "";
        this.data = data;
    }

    public static <T> DataResponse<T> create(T data) {
        return new DataResponse<>(data);
    }
}
