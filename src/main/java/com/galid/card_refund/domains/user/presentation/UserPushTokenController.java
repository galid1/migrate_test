package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.service.UserPushTokenService;
import com.galid.card_refund.domains.user.service.request_response.StorePushTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserPushTokenController {
    private final UserPushTokenService pushTokenService;

    @PostMapping("/users/{userId}/push-token")
    public ResponseEntity storePushToken(@PathVariable("userId") Long userId,
                                         @RequestBody StorePushTokenRequest request) {
        pushTokenService.storeUserPushToken(userId, request);

        return ResponseEntity.ok().build();
    }
}
