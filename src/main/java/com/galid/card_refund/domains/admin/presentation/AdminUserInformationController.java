package com.galid.card_refund.domains.admin.presentation;

import com.galid.card_refund.domains.admin.service.AdminUserInformationService;
import com.galid.card_refund.domains.admin.presentation.request_response.AdminUserInformationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminUserInformationController {
    private final AdminUserInformationService adminUserInformationService;

    @PostMapping("/admin/users/{userId}/information")
    public ResponseEntity addUserInformation(@PathVariable("userId") Long userId, @RequestBody AdminUserInformationRequest request) {
        adminUserInformationService.addUserInformation(userId, request);
        return ResponseEntity.ok().build();
    }
}
