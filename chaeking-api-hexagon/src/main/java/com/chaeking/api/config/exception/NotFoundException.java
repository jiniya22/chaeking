package com.chaeking.api.config.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends ServerException {
    public NotFoundException(String message) {
        super(404, message);
    }
}
