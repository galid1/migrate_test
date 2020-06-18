package com.galid.card_refund.common.config.event;

import com.galid.card_refund.common.pushnotification.FirebaseCloudMessageService;
import com.galid.card_refund.common.pushnotification.PushNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PushNotificationEventHandler {
    private final FirebaseCloudMessageService service;

    @Async
    @EventListener
    public void sendPushNotification(PushNotificationEvent event) {
        service.sendMessageTo(event);
    }
}
