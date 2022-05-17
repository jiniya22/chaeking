package com.chaeking.api.config;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.util.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(InvalidInputException.class)
    protected ResponseEntity<BaseResponse> invalidInputException(InvalidInputException e) {
        return ResponseEntity.badRequest()
                .body(BaseResponse.of(e.getMessage()));
    }

}
