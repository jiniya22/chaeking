package com.chaeking.api.config;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.config.exception.ServerErrorException;
import com.chaeking.api.value.response.BaseResponse;
import com.chaeking.api.value.response.ErrorResponse;
import com.chaeking.api.util.BasicUtils;
import com.chaeking.api.util.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({InvalidInputException.class, ValidationException.class, HttpMessageConversionException.class, IllegalArgumentException.class})
    protected ResponseEntity<BaseResponse> handleException(RuntimeException e) {
        String message = e.getMessage();
        if (e instanceof MethodArgumentTypeMismatchException m) {
            message = String.format("지원하지 않는 %s 입니다. 입력 가능한 값: %s", m.getName(),
                    Arrays.stream(Objects.requireNonNull(m.getRequiredType()).getEnumConstants())
                            .map(Object::toString).collect(Collectors.joining(", ")));
        }
        return ResponseEntity.badRequest()
                .body(BaseResponse.create(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(e.getBindingResult().getFieldErrors().stream().map(ErrorResponse.ErrorMessage::new).collect(Collectors.toList())));
    }

    @ExceptionHandler(ServerErrorException.class)
    protected ResponseEntity<BaseResponse> handleException(ServerErrorException e) {
        return ResponseEntity.internalServerError()
                .body(BaseResponse.create(e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<BaseResponse> handleException(AccessDeniedException e) {
        Long userId = BasicUtils.getUserId();
        if (userId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponse.create(MessageUtils.UNAUTHORIZED_AUTHORIZATION_EMPTY));

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(BaseResponse.create(MessageUtils.FORBIDDEN));
    }

}
