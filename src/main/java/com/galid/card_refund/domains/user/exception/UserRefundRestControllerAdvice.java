package com.galid.card_refund.domains.user.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class UserRefundRestControllerAdvice {
    @ExceptionHandler(NotExistRefundRequestException.class)
    public ResponseEntity handleNotExistRefundRequestException(NotExistRefundRequestException exception) throws IOException {
        // 환급 요청 내역이 존재하지 않습니다.
        return ResponseEntity
                .status(250)
                .body(exception.getMessage());
    }

    @ExceptionHandler(NotYetEstimatedException.class)
    public ResponseEntity handleNotYetEstimatedException(NotYetEstimatedException exception) throws IOException {
        // 아직 평가중인 환급 요청입니다
        return ResponseEntity
                .status(251)
                .body(exception.getMessage());
    }
}
