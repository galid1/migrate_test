package com.galid.card_refund.config;

import com.galid.card_refund.domains.admin.presentation.request_response.AdminEstimateUserPassportRequest;
import com.galid.card_refund.domains.admin.presentation.request_response.AdminRefundEstimateRequest;
import com.galid.card_refund.domains.admin.service.AdminEstimateUserPassportService;
import com.galid.card_refund.domains.admin.service.AdminRefundEstimateService;
import com.galid.card_refund.domains.card.card.domain.CardEntity;
import com.galid.card_refund.domains.refund.domain.RefundEntity;
import com.galid.card_refund.domains.refund.domain.RefundRepository;
import com.galid.card_refund.domains.user.domain.UsageHistory;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserPassportStatus;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.UserCardService;
import com.galid.card_refund.domains.user.service.UserRefundService;
import com.galid.card_refund.domains.user.service.request_response.UserRefundRequest;
import com.galid.card_refund.domains.user.service.request_response.UserRefundResponse;
import com.galid.card_refund.domains.user.service.request_response.UserRegisterCardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class UserSetUp {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefundRepository refundRepository;
    @Autowired
    private UserCardService userCardService;
    @Autowired
    private UserRefundService userRefundService;
    @Autowired
    private AdminEstimateUserPassportService userPassportEstimateService;
    @Autowired
    private AdminRefundEstimateService adminRefundEstimateService;

    public UserEntity saveUser() {
        String TEST_DEVICE_ID = "TEST";
        String TEST_NICKNAME = "TEST";
        String TEST_PASSPORT_IMAGE = "TEST";

        UserEntity savedUser = userRepository.save(UserEntity.builder()
                .deviceId(TEST_DEVICE_ID)
                .nickname(TEST_NICKNAME)
                .build());
        savedUser.uploadPassportImagePath(TEST_PASSPORT_IMAGE);

        return savedUser;
    }

    public void registerCard(UserEntity TEST_USER_ENTITY, CardEntity TEST_CARD_ENTITY) {
        userCardService.registerCard(TEST_USER_ENTITY.getUserId(),
                new UserRegisterCardRequest(
                        TEST_CARD_ENTITY.getCardInformation().getCardNum(),
                        TEST_CARD_ENTITY.getCardInformation().getSerial()
                ));
    }

    public void saveUserCardUsageHistory(UserEntity userEntity, UsageHistory history) {
        userEntity.recordCardUsage(history);
    }

    public void estimateUserPassport(UserEntity userEntity) {
        userPassportEstimateService.addUserInformation(userEntity.getUserId(),
                AdminEstimateUserPassportRequest.builder()
                        .address("TEST")
                        .estimateResultStatus(UserPassportStatus.SUCCESS_STATUS)
                        .nation("TEST")
                        .passportNum("TEST")
                        .name("TEST")
                        .build());
    }

    public RefundEntity saveUserRefundRequest(UserEntity userEntity, List<UserRefundRequest> refundRequestList, Map<String, byte[]> refundItemImageBytMap) {
        UserRefundResponse refund = userRefundService.refund(userEntity.getUserId(),
                refundRequestList,
                refundItemImageBytMap
        );
        return refundRepository.findById(refund.getRefundId()).get();
    }

    public void estimateRefundRequest(Long refundId) {
        adminRefundEstimateService.estimateRefundRequest(refundId,
                AdminRefundEstimateRequest.builder()
                        .refundEstimateLineList(List.of(
                                new AdminRefundEstimateRequest.RefundEstimateLineRequest("TEST", 1000)
                        ))
                        .unRefundableLineDescription("TEST")
                        .build(),
                "TEST".getBytes());
    }
}
