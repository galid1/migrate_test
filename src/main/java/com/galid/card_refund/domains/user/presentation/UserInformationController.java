package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.service.UserInformationService;
import com.galid.card_refund.domains.user.service.request_response.UserInformationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserInformationController {
    private final UserInformationService userInformationService;

    @PostMapping("/admin/users/{userId}/information")
    public ResponseEntity addUserInformation(@PathVariable("userId") Long userId, @RequestBody UserInformationRequest request) {
        userInformationService.addUserInformation(userId, request);
        return ResponseEntity.ok().build();
    }
}
