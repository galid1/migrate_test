package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserInformation;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.UserInformationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInformationService {
    private final UserRepository userRepository;

    public void addUserInformation(Long userId, UserInformationRequest request) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        userEntity.addUserInformation(UserInformation.builder()
                .address(request.getAddress())
                .name(request.getName())
                .nation(request.getNation())
                .passportNum(request.getPassportNum())
                .build());
    }
}
