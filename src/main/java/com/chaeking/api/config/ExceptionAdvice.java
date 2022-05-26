package com.chaeking.api.config;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.domain.value.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ValidationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({InvalidInputException.class, ValidationException.class, HttpMessageConversionException.class})
    protected ResponseEntity<BaseResponse> invalidInputException(RuntimeException e) {
        return ResponseEntity.badRequest()
                .body(BaseResponse.of(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(e.getBindingResult().getFieldErrors().stream().map(ErrorResponse.ErrorMessage::new).collect(Collectors.toList())));
    }

}
