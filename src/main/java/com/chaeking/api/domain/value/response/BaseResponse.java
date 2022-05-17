package com.chaeking.api.domain.value.response;

import com.chaeking.api.util.MessageUtils;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BaseResponse {
    private String result;
    private String reason;

    protected BaseResponse() {
        this.result = MessageUtils.SUCCESS;
        this.reason = "";
    }

    private BaseResponse(String reason) {
        this.result = MessageUtils.FAIL;
        this.reason = reason;
    }

    public static BaseResponse of() {
        return new BaseResponse();
    }
    public static BaseResponse of(String reason) {
        return new BaseResponse(reason);
    }

}
