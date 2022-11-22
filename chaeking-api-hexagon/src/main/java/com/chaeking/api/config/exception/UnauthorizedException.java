package com.chaeking.api.config.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends ServerException {
    public UnauthorizedException() {
        super(401, "access_token 이 유효하지 않습니다.");
    }
    public UnauthorizedException(String message) {
        super(401, message);
    }
}
