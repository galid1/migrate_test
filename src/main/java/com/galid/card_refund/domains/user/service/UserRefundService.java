package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.refund.refund.domain.RefundEntity;
import com.galid.card_refund.domains.refund.refund.domain.RefundLine;
import com.galid.card_refund.domains.refund.refund.domain.RefundRepository;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.UserRefundResultResponse;
import com.galid.card_refund.domains.user.service.request_response.UserRefundRequest;
import com.galid.card_refund.domains.user.service.request_response.UserRefundResponse;
import com.galid.card_refund.domains.user.service.request_response.UserRefundResultResponse.RefundResultResponseLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRefundService {
    private final RefundRepository refundRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserRefundResponse refund(List<UserRefundRequest> refundLineList, Long requestorId) {
        RefundEntity refundEntity = RefundEntity.builder()
                .requestRefundLine(toRefundLineList(refundLineList))
                .requestorId(requestorId)
                .build();

        return new UserRefundResponse(refundRepository.save(refundEntity)
                .getExpectRefundAmount()
                .getValue());
    }

    private List<RefundLine> toRefundLineList(List<UserRefundRequest> refundRequests) {
        return refundRequests.stream()
                .map(userRefundRequest ->
                        RefundLine.builder()
                                .place(userRefundRequest.getPlace())
                                .paymentAmount(new Money(userRefundRequest.getPaymentAmount()))
                                .itemImageUrl(userRefundRequest.getBase64File())
                                .purchaseDateTime(userRefundRequest.getPurchaseDateTime())
                                .build()
                )
                .collect(Collectors.toList());
    }

    public UserRefundResultResponse getRefundRequestResult(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        RefundEntity refundEntity = refundRepository.findByRequestorId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        return UserRefundResultResponse.builder()
                .refundResultResponseLineList(refundEntity.getRefundResultLineList().stream()
                        .map(r -> RefundResultResponseLine.builder()
                                .paymentAmount(r.getPaymentAmount().getValue())
                                .refundAmount(r.getRefundAmount().getValue())
                                .place(r.getPlace())
                                .build())
                        .collect(Collectors.toList()))
                .userInformation(userEntity.getUserInformation())
                .unRefundableLineDescription(refundEntity.getUnRefundableLineDescription())
                .build();
    }
}
