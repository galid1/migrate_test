package com.galid.card_refund.domains.admin.application;

import com.galid.card_refund.common.aws.ImageType;
import com.galid.card_refund.common.aws.S3FileUploader;
import com.galid.card_refund.domains.admin.application.request_response.AdminRefundEstimateRequest;
import com.galid.card_refund.domains.refund.domain.RefundEntity;
import com.galid.card_refund.domains.refund.domain.RefundRepository;
import com.galid.card_refund.domains.refund.domain.RefundResultLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEstimateRefundService {
    private final RefundRepository refundRepository;
    private final S3FileUploader s3FileUploader;

    @Transactional
    public void estimateRefundRequest(Long refundId, AdminRefundEstimateRequest request) {
        RefundEntity refundEntity = refundRepository.findById(refundId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 환급 요청입니다."));

        List<RefundResultLine> refundableLineList = makeRefundableLineList(request);

        refundEntity.estimate(refundableLineList,
                              request.getUnRefundableLineDescription(),
                              s3FileUploader.uploadFile(String.valueOf(refundId), ImageType.BARCODE_IMAGE, request.getBarcodeImageByte()));
    }

    private RefundResultLine toRefundResultLine(AdminRefundEstimateRequest.RefundEstimateLineRequest request) {
        return RefundResultLine.builder()
                .place(request.getPlaceAndName())
                .paymentAmount(request.getPaymentAmount())
                .build();
    }

    private List<RefundResultLine> makeRefundableLineList(AdminRefundEstimateRequest request) {
        return request.getRefundEstimateLineList()
                .stream()
                .map(refundEstimateLine -> toRefundResultLine(refundEstimateLine))
                .collect(Collectors.toList());
    }
}
