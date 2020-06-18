package com.galid.card_refund.domains.admin.service;

import com.galid.card_refund.common.pushnotification.PushNotificationEvent;
import com.galid.card_refund.domains.admin.service.request_response.AdminEstimateUserPassportRequest;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserPassportInformation;
import com.galid.card_refund.domains.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminEstimateUserPassportService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void addUserInformation(Long userId, AdminEstimateUserPassportRequest request) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        userEntity.estimatePassport(request.getEstimateResultStatus(), UserPassportInformation.builder()
                .address(request.getAddress())
                .name(request.getName())
                .nation(request.getNation())
                .passportNum(request.getPassportNum())
                .build());

        eventPublisher.publishEvent(new PushNotificationEvent(userId, "여권정보 평가완료.", "여권 정보 평가가 완료되었습니다."));
    }
}
