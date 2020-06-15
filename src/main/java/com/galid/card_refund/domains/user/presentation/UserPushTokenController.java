package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.service.UserPushTokenService;
import com.galid.card_refund.domains.user.service.request_response.StorePushTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users/{userId}/push-token")
public class UserPushTokenController {
    private final UserPushTokenService pushTokenService;

    @PostMapping
    public ResponseEntity storePushToken(@PathVariable("userId") Long userId,
                                         @RequestBody StorePushTokenRequest request) {
        pushTokenService.storeUserPushToken(userId, request);

        return ResponseEntity.ok().build();
    }
    

}
