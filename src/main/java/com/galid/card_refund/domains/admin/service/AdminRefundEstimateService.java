package com.galid.card_refund.domains.admin.service;

import com.galid.card_refund.common.aws.S3FileUploader;
import com.galid.card_refund.domains.refund.refund.domain.RefundEntity;
import com.galid.card_refund.domains.refund.refund.domain.RefundRepository;
import com.galid.card_refund.domains.refund.refund.domain.RefundResultLine;
import com.galid.card_refund.domains.admin.presentation.request_response.AdminRefundEstimateRequest;
import com.galid.card_refund.domains.admin.presentation.request_response.AdminRefundEstimateRequest.RefundEstimateLineRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminRefundEstimateService {
    private final RefundRepository refundRepository;
    private final S3FileUploader s3FileUploader;
    private String UPLOAD_PATH_KEY = "refund-result-barcode";

    @Transactional
    public void estimateRefundRequest(Long refundId, AdminRefundEstimateRequest request, byte[] refundResultBarcodeImageBytes) {
        RefundEntity refundEntity = refundRepository.findById(refundId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 환급 요청입니다."));

        List<RefundResultLine> refundableLineList = request.getRefundEstimateLineList()
                .stream()
                .map(refundEstimateLine -> toRefundResultLine(refundEstimateLine))
                .collect(Collectors.toList());


        refundEntity.estimate(refundableLineList,
                              request.getUnRefundableLineDescription(),
                              s3FileUploader.uploadFile(makeS3UploadPath(refundId), refundResultBarcodeImageBytes));
    }

    private String makeS3UploadPath(long refundId) {
        return UPLOAD_PATH_KEY + "/" + refundId;
    }

    private RefundResultLine toRefundResultLine(RefundEstimateLineRequest request) {
        return RefundResultLine.builder()
                .place(request.getPlaceAndName())
                .paymentAmount(request.getPaymentAmount())
                .build();
    }
}
