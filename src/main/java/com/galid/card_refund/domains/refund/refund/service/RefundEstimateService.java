package com.galid.card_refund.domains.refund.refund.service;

import com.galid.card_refund.domains.refund.refund.domain.RefundEntity;
import com.galid.card_refund.domains.refund.refund.domain.RefundRepository;
import com.galid.card_refund.domains.refund.refund.domain.RefundResultLine;
import com.galid.card_refund.domains.refund.refund.service.request_response.RefundEstimateRequest;
import com.galid.card_refund.domains.refund.refund.service.request_response.RefundEstimateRequest.RefundEstimateLine;
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

        List<RefundResultLine> refundableLineList = request.getRefundEstimateLineList()
                .stream()
                .map(refundEstimateLine -> toRefundResultLine(refundEstimateLine))
                .collect(Collectors.toList());


        refundEntity.estimate(refundableLineList, request.getUnRefundableLineDescription());
    }

    private RefundResultLine toRefundResultLine(RefundEstimateLine request) {
        return RefundResultLine.builder()
                .place(request.getPlaceAndName())
                .paymentAmount(request.getPaymentAmount())
                .build();
    }
}
