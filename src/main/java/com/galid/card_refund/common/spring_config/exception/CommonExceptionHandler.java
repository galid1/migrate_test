package com.galid.card_refund.common.spring_config.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CommonExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ExceptionResponse illegalExceptionHandle(Exception exception) {
        return new ExceptionResponse(exception.getMessage());
    }

    @Getter
    @Data
    @AllArgsConstructor
    class ExceptionResponse {
        private String errorMessage;
    }
}
