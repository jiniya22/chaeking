package xyz.applebox.book.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import xyz.applebox.book.config.exception.InvalidInputException;
import xyz.applebox.book.domain.dto.response.BaseResponse;
import xyz.applebox.book.util.MessageUtils;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(InvalidInputException.class)
    protected ResponseEntity<BaseResponse> invalidInputException(InvalidInputException e) {
        return ResponseEntity.badRequest()
                .body(new BaseResponse(MessageUtils.FAIL, e.getMessage()));
    }

}
