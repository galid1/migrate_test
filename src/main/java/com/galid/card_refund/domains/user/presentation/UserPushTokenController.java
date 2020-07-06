package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.application.UserPushTokenService;
import com.galid.card_refund.domains.user.application.request_response.GetPushTokenResponse;
import com.galid.card_refund.domains.user.application.request_response.StorePushTokenRequest;
import com.galid.card_refund.domains.user.application.request_response.UpdatePushTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users/{userId}/push-token")
public class UserPushTokenController {
    private final UserPushTokenService pushTokenService;

    @PostMapping
    public ResponseEntity storePushToken(@PathVariable("userId") Long userId,
                                         @RequestBody @Valid StorePushTokenRequest request) {
        pushTokenService.storeUserPushToken(userId, request);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity updatePushToken(@PathVariable("userId") Long userId,
                                          @RequestBody @Valid UpdatePushTokenRequest request) {
        pushTokenService.updatePushToken(userId, request);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public GetPushTokenResponse getPushToken(@PathVariable("userId") Long userId) {
        return pushTokenService.getPushToken(userId);
    }
}
