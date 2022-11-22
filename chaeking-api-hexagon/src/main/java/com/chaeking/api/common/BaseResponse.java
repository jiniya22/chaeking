package com.chaeking.api.common;

import lombok.Getter;

@Getter
public class BaseResponse {
    private String result;
    private String reason;

    public static BaseResponse SUCCESS = new BaseResponse();

    private BaseResponse() {
        this.result = "success";
        this.reason = "";
    }
    public BaseResponse(String reason) {
        this.result = "fail";
        this.reason = reason;
    }
}
