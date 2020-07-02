package com.galid.card_refund.config;

import com.galid.card_refund.common.pushnotification.FirebaseCloudMessageService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@Component
@Profile("test")
public class PushNotificationSetUp {
    @MockBean
    private FirebaseCloudMessageService service;

    @PostConstruct
    public void init() {
        doNothing().when(service).sendMessageTo(any(), any(), any());
    }
}
