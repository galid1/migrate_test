package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.refund.refund.domain.RefundEntity;
import com.galid.card_refund.domains.refund.refund.domain.RefundLine;
import com.galid.card_refund.domains.refund.refund.domain.RefundRepository;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.RefundableResponse;
import com.galid.card_refund.domains.user.service.request_response.UnRefundableLineResponse;
import com.galid.card_refund.domains.user.service.request_response.UserRefundRequest;
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
    public double refund(List<UserRefundRequest> refundLineList, Long requestorId) {
        RefundEntity refundEntity = RefundEntity.builder()
                .requestRefundLine(toRefundLineList(refundLineList))
                .requestorId(requestorId)
                .build();

        return refundRepository.save(refundEntity)
                .getExpectRefundAmount()
                .getValue();
    }

    private List<RefundLine> toRefundLineList(List<UserRefundRequest> refundRequests) {
        return refundRequests.stream()
                .map(userRefundRequest ->
                    RefundLine.builder()
                        .place(userRefundRequest.getPlace())
                        .paymentAmount(new Money(userRefundRequest.getPaymentAmount()))
                            .itemImageUrl(userRefundRequest.getBase64File())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public RefundableResponse getRefundable(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        RefundEntity refundEntity = refundRepository.findByRequestorId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        return RefundableResponse.builder()
                .refundableLineList(refundEntity.getRefundableLineList().stream()
                        .map(refundLine -> new RefundableResponse.RefundableLineResponse(refundLine.getPlace(), refundLine.getPaymentAmount()))
                        .collect(Collectors.toList()))
                .userInformation(userEntity.getUserInformation())
                .build();
    }

    public List<UnRefundableLineResponse> getUnRefundable(Long userId) {
        RefundEntity refundEntity = refundRepository.findByRequestorId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        return refundEntity.getUnRefundableLineList()
                .stream()
                .map(unRefundableLine -> UnRefundableLineResponse.builder()
                        .paymentAmount(unRefundableLine.getPaymentAmount())
                        .place(unRefundableLine.getPlace())
                        .refundItemImageUrl(unRefundableLine.getRefundItemImageUrl())
                        .unRefundableReason(unRefundableLine.getUnRefundableReason())
                        .build())
                .collect(Collectors.toList());
    }
}
