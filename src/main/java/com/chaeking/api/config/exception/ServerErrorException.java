package com.chaeking.api.config.exception;

public class ServerErrorException extends RuntimeException {
    public ServerErrorException() {
        super("서버 장애");
    }
    public ServerErrorException(String msg) {
        super(msg);
    }
}
