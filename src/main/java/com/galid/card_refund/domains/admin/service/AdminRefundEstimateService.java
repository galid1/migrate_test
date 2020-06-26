package com.galid.card_refund.domains.admin.service;

import com.galid.card_refund.common.aws.ImageType;
import com.galid.card_refund.common.aws.S3FileUploader;
import com.galid.card_refund.common.pushnotification.PushNotificationEvent;
import com.galid.card_refund.domains.refund.domain.RefundEntity;
import com.galid.card_refund.domains.refund.domain.RefundRepository;
import com.galid.card_refund.domains.refund.domain.RefundResultLine;
import com.galid.card_refund.domains.admin.service.request_response.AdminRefundEstimateRequest;
import com.galid.card_refund.domains.admin.service.request_response.AdminRefundEstimateRequest.RefundEstimateLineRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminRefundEstimateService {
    private final RefundRepository refundRepository;
    private final S3FileUploader s3FileUploader;
    private final ApplicationEventPublisher eventPublisher;

    public void estimateRefundRequest(Long refundId, AdminRefundEstimateRequest request, byte[] refundResultBarcodeImageBytes) {
        RefundEntity refundEntity = refundRepository.findById(refundId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 환급 요청입니다."));

        List<RefundResultLine> refundableLineList = request.getRefundEstimateLineList()
                .stream()
                .map(refundEstimateLine -> toRefundResultLine(refundEstimateLine))
                .collect(Collectors.toList());

        refundEntity.estimate(refundableLineList,
                              request.getUnRefundableLineDescription(),
                              s3FileUploader.uploadFile(String.valueOf(refundId), ImageType.BARCODE_IMAGE, refundResultBarcodeImageBytes));

        eventPublisher.publishEvent(new PushNotificationEvent(refundEntity.getRequestorId(), "환급평가 완료", "환급 평가가 완료되었습니다."));
    }



    private RefundResultLine toRefundResultLine(RefundEstimateLineRequest request) {
        return RefundResultLine.builder()
                .place(request.getPlaceAndName())
                .paymentAmount(request.getPaymentAmount())
                .build();
    }
}
