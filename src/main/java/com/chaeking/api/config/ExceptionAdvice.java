package com.chaeking.api.config;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.util.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ValidationException;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({InvalidInputException.class, ValidationException.class, HttpMessageConversionException.class})
    protected ResponseEntity<BaseResponse> invalidInputException(RuntimeException e) {
        return ResponseEntity.badRequest()
                .body(BaseResponse.of(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<BaseResponse> handleException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst().map(fieldError ->
                        String.format("%s 오류. %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .orElse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.of(message));
    }

}
