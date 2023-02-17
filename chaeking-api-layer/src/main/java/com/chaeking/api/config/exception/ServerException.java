package com.chaeking.api.config.exception;

import lombok.Getter;

public class ServerException extends RuntimeException {
    @Getter private int code;

    public ServerException(String msg) {
        super(msg);
        this.code = 500;
    }
    public ServerException(int code, String msg) {
        super(msg);
        this.code = code;
    }

}
