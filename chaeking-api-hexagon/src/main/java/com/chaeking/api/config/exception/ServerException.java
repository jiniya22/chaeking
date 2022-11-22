package com.chaeking.api.config.exception;

import lombok.Getter;

@Getter
public class ServerException extends RuntimeException {
    private int code;
    private String message;

    public ServerException(String message) {
        this.code = 500;
        this.message = message;
    }

    public ServerException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
