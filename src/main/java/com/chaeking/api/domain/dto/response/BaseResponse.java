package com.chaeking.api.domain.dto.response;

import lombok.Getter;
import lombok.Setter;
import com.chaeking.api.util.MessageUtils;

@Getter @Setter
public class BaseResponse {
    private String result;
    private String reason;

    public BaseResponse() {
        this.result = MessageUtils.SUCCESS;
        this.reason = "";
    }

    public BaseResponse(String result, String reason) {
        this.result = result;
        this.reason = reason;
    }
}
