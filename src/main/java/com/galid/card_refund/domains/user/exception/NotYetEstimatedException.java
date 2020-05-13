package com.galid.card_refund.domains.user.exception;

public class NotYetEstimatedException extends RuntimeException{
    public NotYetEstimatedException() {
        super("아직 평가중인 환급 요청입니다.");
    }
}
