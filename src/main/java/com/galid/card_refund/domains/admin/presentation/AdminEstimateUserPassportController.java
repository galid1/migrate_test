package com.galid.card_refund.domains.admin.presentation;

import com.galid.card_refund.domains.admin.service.AdminEstimateUserPassportService;
import com.galid.card_refund.domains.admin.service.request_response.AdminEstimateUserPassportRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminEstimateUserPassportController {
    private final AdminEstimateUserPassportService adminEstimateUserPassportService;

    @PostMapping("/admin/users/{userId}/passport")
    public ResponseEntity addUserInformation(@PathVariable("userId") Long userId, @RequestBody AdminEstimateUserPassportRequest request) {
        adminEstimateUserPassportService.addUserInformation(userId, request);
        return ResponseEntity.ok().build();
    }
}
