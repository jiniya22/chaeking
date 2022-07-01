package com.chaeking.api.domain.value.response;

import com.chaeking.api.util.MessageUtils;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BaseResponse {
    private String result;
    private String reason;

    private static BaseResponse INSTANCE;

    protected BaseResponse() {
        this.result = MessageUtils.SUCCESS;
        this.reason = "";
    }

    private BaseResponse(String reason) {
        this.result = MessageUtils.FAIL;
        this.reason = reason;
    }

    public static BaseResponse of() {
        if(INSTANCE == null)
            INSTANCE = new BaseResponse();
        return INSTANCE;
    }
    public static BaseResponse of(String reason) {
        return new BaseResponse(reason);
    }

}
