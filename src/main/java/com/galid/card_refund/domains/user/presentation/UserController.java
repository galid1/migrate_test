package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.service.UserInformationService;
import com.galid.card_refund.domains.user.service.request_response.UserInformationResponse;
import com.galid.card_refund.domains.user.service.request_response.UserPassportStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserInformationService service;

    @GetMapping("/users/{userId}/information")
    public UserInformationResponse getUserInformation(@PathVariable("userId") Long userId) {
        return service.getUserInformation(userId);
    }

    @GetMapping("/users/{userId}/passport-status")
    public UserPassportStatusResponse getUserPassportStatus(@PathVariable("userId") Long userId) {
        return service.getUserPassportStatus(userId);
    }

}
