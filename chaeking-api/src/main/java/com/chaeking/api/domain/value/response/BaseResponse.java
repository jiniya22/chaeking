package com.chaeking.api.domain.value.response;

import com.chaeking.api.util.MessageUtils;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BaseResponse {
    private String result;
    private String reason;

    public static final BaseResponse SUCCESS_INSTANCE = new BaseResponse();

    protected BaseResponse() {
        this.result = MessageUtils.SUCCESS;
        this.reason = "";
    }

    private BaseResponse(String reason) {
        this.result = MessageUtils.FAIL;
        this.reason = reason;
    }

    public static BaseResponse create(String reason) {
        return new BaseResponse(reason);
    }

}
