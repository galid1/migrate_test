package com.galid.card_refund.common.pushnotification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class PushNotificationEvent {
    private Long userId;
    private String title;
    private String body;
}
