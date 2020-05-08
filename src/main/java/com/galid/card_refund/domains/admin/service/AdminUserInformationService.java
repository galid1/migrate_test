package com.galid.card_refund.domains.admin.service;

import com.galid.card_refund.domains.admin.presentation.request_response.AdminUserInformationRequest;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserPassportInformation;
import com.galid.card_refund.domains.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserInformationService {
    private final UserRepository userRepository;

    @Transactional
    public void addUserInformation(Long userId, AdminUserInformationRequest request) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        userEntity.estimatePassport(UserPassportInformation.builder()
                .address(request.getAddress())
                .name(request.getName())
                .nation(request.getNation())
                .passportNum(request.getPassportNum())
                .build());
    }
}
