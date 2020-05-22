package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.UserInformationResponse;
import com.galid.card_refund.domains.user.service.request_response.UserPassportImageResponse;
import com.galid.card_refund.domains.user.service.request_response.UserPassportStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInformationService {
    private final UserRepository userRepository;

    public UserInformationResponse getUserInformation(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        return UserInformationResponse.builder()
                .nickname(userEntity.getNickname())
                .passportImageUrl(userEntity.getPassPortImagePath())
                .build();
    }

    public UserPassportStatusResponse getUserPassportStatus(Long userId) {
        UserEntity userEntity = userRepository.findById((userId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        return new UserPassportStatusResponse(userEntity.getPassportStatus());
    }

    public UserPassportImageResponse getUserPassportImage(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        return new UserPassportImageResponse(userEntity.getPassPortImagePath());
    }
}
