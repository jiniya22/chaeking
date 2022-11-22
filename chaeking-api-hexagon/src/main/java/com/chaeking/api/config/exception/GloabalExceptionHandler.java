package com.chaeking.api.config.exception;

import com.chaeking.api.common.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GloabalExceptionHandler {

    @ExceptionHandler(ServerException.class)
    protected ResponseEntity<BaseResponse> handleException(ServerException e) {
        return ResponseEntity.status(e.getCode())
                .body(new BaseResponse(e.getMessage()));
    }

}
