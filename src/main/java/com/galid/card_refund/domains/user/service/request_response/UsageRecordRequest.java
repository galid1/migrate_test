package com.galid.card_refund.domains.user.service.request_response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class UsageRecordRequest {
    private String date;
    private String from;
    private String to;
    private String message;
}
