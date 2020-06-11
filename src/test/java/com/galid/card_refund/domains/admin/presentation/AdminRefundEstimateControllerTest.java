package com.galid.card_refund.domains.admin.presentation;

import com.galid.card_refund.common.BaseIntegrationTest;
import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.config.UserSetUp;
import com.galid.card_refund.domains.admin.service.request_response.AdminRefundEstimateRequest;
import com.galid.card_refund.domains.refund.domain.RefundEntity;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.service.request_response.UserRefundRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRefundEstimateControllerTest extends BaseIntegrationTest {
    @Autowired
    private UserSetUp userSetUp;

    private RefundEntity TEST_REFUND_ENTITY;

    @BeforeEach
    public void init() {
        UserEntity TEST_USER_ENTITY = userSetUp.saveUser();
        int firstRefundItemId = 1;
        int secondRefundItemId = 2;

        List<UserRefundRequest> refundRequestList = List.of(
                UserRefundRequest.builder()
                        .refundItemId(firstRefundItemId)
                        .purchaseDateTime("TEST")
                        .place("TEST")
                        .paymentAmount(new Money(1000).getValue())
                        .build(),
                UserRefundRequest.builder()
                        .refundItemId(secondRefundItemId)
                        .place("TEST")
                        .purchaseDateTime("TEST")
                        .paymentAmount(new Money(2000).getValue())
                        .build()
        );
        Map<String, byte[]> refundItemImageByteMap = Map.ofEntries(
                entry(String.valueOf(firstRefundItemId), "TEST".getBytes()),
                entry(String.valueOf(secondRefundItemId), "TEST".getBytes())
        );

        TEST_REFUND_ENTITY = userSetUp.saveUserRefundRequest(TEST_USER_ENTITY, refundRequestList, refundItemImageByteMap);
    }

    @Test
    public void 환급요청평가() throws Exception {
        //given
        AdminRefundEstimateRequest request = new AdminRefundEstimateRequest(
                List.of(
                        new AdminRefundEstimateRequest.RefundEstimateLineRequest("TEST", 1000)
                ),
                "TEST");

        MockMultipartFile barcodeImageFile = new MockMultipartFile("test",
                "test.txt",
                "image/png",
                "test".getBytes());

        //when
        ResultActions resultActions = mvc.perform(multipart("/admin/refunds/{refundId}", TEST_REFUND_ENTITY.getRefundId())
                .file("barcodeImage", barcodeImageFile.getBytes())
                .param("estimateInformation", objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk());

        //rest docs
        resultActions
                .andDo(document("admin/{method-name}",
                        requestParts(
                                partWithName("barcodeImage").description("환급 결과 바코드 이미지")
                        ),
                        requestParameters(
                                parameterWithName("estimateInformation").description("환급 평가 정보 \n ex. \n {refundEstimateLineList:[{'placeAndName':'TEST', 'paymentAmount':'1000'}], unRefundableLineDescription:'asd'}")
                        )
                ));
    }

}