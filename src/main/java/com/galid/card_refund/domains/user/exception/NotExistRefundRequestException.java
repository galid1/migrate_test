package com.galid.card_refund.domains.user.exception;

public class NotExistRefundRequestException extends RuntimeException{
    public NotExistRefundRequestException() {
        super("환급 요청 내역이 존재하지 않습니다.");
    }
}
