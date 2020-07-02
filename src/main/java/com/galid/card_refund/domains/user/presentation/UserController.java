package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.application.UserInformationService;
import com.galid.card_refund.domains.user.application.request_response.UserInformationResponse;
import com.galid.card_refund.domains.user.application.request_response.UserInformationUpdateRequest;
import com.galid.card_refund.domains.user.application.request_response.UserPassportStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users/{userId}")
public class UserController {
    private final UserInformationService service;

    @GetMapping("/information")
    public UserInformationResponse getUserInformation(@PathVariable("userId") Long userId) {
        return service.getUserInformation(userId);
    }

    @PutMapping("/information")
    public ResponseEntity updateUserInformation(@PathVariable("userId") Long userId,
                                                @ModelAttribute UserInformationUpdateRequest request) throws IOException {
        request.userPassportImageToByte();
        service.updateUserInformation(userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/passport-status")
    public UserPassportStatusResponse getUserPassportStatus(@PathVariable("userId") Long userId) {
        return service.getUserPassportStatus(userId);
    }

}
