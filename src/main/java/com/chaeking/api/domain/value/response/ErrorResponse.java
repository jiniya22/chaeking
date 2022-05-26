package com.chaeking.api.domain.value.response;

import com.chaeking.api.util.MessageUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ErrorResponse {
    private String result;
    private List<ErrorMessage> reason;

    private ErrorResponse() {
        this.result = MessageUtils.FAIL;
        this.reason = new ArrayList<>();
    }

    private ErrorResponse(List<ErrorMessage> reason) {
        this.result = MessageUtils.FAIL;
        this.reason = reason;
    }

    public static ErrorResponse of() {
        return new ErrorResponse();
    }
    public static ErrorResponse of(List<ErrorMessage> reason) {
        return new ErrorResponse(reason);
    }

    public record ErrorMessage(String field, String message) {
        public ErrorMessage(FieldError e) {
            this(e.getField(), e.getDefaultMessage());
        }
    }
}
