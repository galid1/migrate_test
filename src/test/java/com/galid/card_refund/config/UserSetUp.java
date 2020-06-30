package com.galid.card_refund.config;

import com.galid.card_refund.domains.admin.application.request_response.AdminEstimateUserPassportRequest;
import com.galid.card_refund.domains.admin.application.request_response.AdminRefundEstimateRequest;
import com.galid.card_refund.domains.admin.application.AdminEstimateUserPassportService;
import com.galid.card_refund.domains.admin.application.AdminRefundEstimateService;
import com.galid.card_refund.domains.card.domain.CardEntity;
import com.galid.card_refund.domains.refund.domain.RefundEntity;
import com.galid.card_refund.domains.refund.domain.RefundRepository;
import com.galid.card_refund.domains.user.domain.UsageHistory;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserPassportStatus;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.application.UserCardService;
import com.galid.card_refund.domains.user.application.UserPushTokenService;
import com.galid.card_refund.domains.user.application.UserRefundService;
import com.galid.card_refund.domains.user.application.UserSignInService;
import com.galid.card_refund.domains.user.application.request_response.*;
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
    private UserSignInService userSignInService;
    @Autowired
    private UserCardService userCardService;
    @Autowired
    private UserRefundService userRefundService;
    @Autowired
    private UserPushTokenService userPushTokenService;
    @Autowired
    private AdminEstimateUserPassportService userPassportEstimateService;
    @Autowired
    private AdminRefundEstimateService adminRefundEstimateService;

    private String TEST_DEVICE_ID;
    private String TEST_NICKNAME;
    private String TEST_PASSPORT_IMAGE;

    public UserEntity saveUser() {
        TEST_DEVICE_ID = "TEST";
        TEST_NICKNAME = "TEST";
        TEST_PASSPORT_IMAGE = "TEST";

        UserEntity savedUser = userRepository.save(UserEntity.builder()
                .deviceId(TEST_DEVICE_ID)
                .nickname(TEST_NICKNAME)
                .build());
        savedUser.uploadPassportImagePath(TEST_PASSPORT_IMAGE);

        return savedUser;
    }

    public String signIn() {
        UserSignInRequest request = new UserSignInRequest(TEST_DEVICE_ID);
        return userSignInService.signIn(request).getApiToken();
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

    public void savePushToken(UserEntity userEntity, String pushToken) {
        userPushTokenService.storeUserPushToken(userEntity.getUserId(), new StorePushTokenRequest(pushToken));
    }
}
