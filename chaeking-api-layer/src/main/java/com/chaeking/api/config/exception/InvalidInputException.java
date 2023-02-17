package com.chaeking.api.config.exception;

public class InvalidInputException extends ServerException {
    public InvalidInputException() {
        super(400, "입력 값이 잘못되었습니다.");
    }
    public InvalidInputException(String message) {
        super(400, message);
    }
}
