package com.galid.card_refund.domains.refund.refund.service;

import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.refund.refund.domain.RefundEntity;
import com.galid.card_refund.domains.refund.refund.domain.RefundLine;
import com.galid.card_refund.domains.refund.refund.domain.RefundRepository;
import com.galid.card_refund.domains.refund.refund.service.request_response.RefundEstimateRequest;
import com.galid.card_refund.domains.refund.refund.service.request_response.RefundableLineRequest;
import com.galid.card_refund.domains.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefundEstimateService {
    private final RefundRepository refundRepository;

    @Transactional
    public void estimateRefundRequest(Long refundId, RefundEstimateRequest request) {
        RefundEntity refundEntity = refundRepository.findById(refundId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 환급 요청입니다."));

        List<RefundLine> refundableLineList = request.getRefundableLineRequestList()
                .stream()
                .map(refundableLineRequest -> toRefundLine(refundableLineRequest))
                .collect(Collectors.toList());


        refundEntity.estimate(refundableLineList, request.getUnRefundableLineDescription());
    }

    private RefundLine toRefundLine(RefundableLineRequest request) {
        return RefundLine.builder()
                .paymentAmount(new Money(request.getPaymentAmount()))
                .itemImageUrl(request.getItemImageUrl())
                .place(request.getPlaceAndName())
                .build();
    }
}
