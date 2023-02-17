package com.chaeking.api.config.exception;

public class UnauthorizedException extends ServerException {
    public UnauthorizedException() {
        super(404, "인증 정보가 잘못되었습니다.");
    }
}
