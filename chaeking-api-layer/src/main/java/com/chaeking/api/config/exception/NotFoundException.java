package com.chaeking.api.config.exception;

public class NotFoundException extends ServerException {
    public NotFoundException(String msg) {
        super(404, msg);
    }
}
