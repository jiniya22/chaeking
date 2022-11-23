package com.chaeking.api.config.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends ServerException {
    public BadRequestException(String message) {
        super(400, message);
    }
}
