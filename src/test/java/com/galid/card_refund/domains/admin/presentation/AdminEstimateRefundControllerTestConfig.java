package com.galid.card_refund.domains.admin.presentation;

import com.galid.card_refund.common.BaseIntegrationTestConfig;
import com.galid.card_refund.config.UserSetUp;
import com.galid.card_refund.domains.refund.domain.RefundEntity;
import com.galid.card_refund.domains.user.application.request_response.UserRefundRequestList;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.application.request_response.UserRefundRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminEstimateRefundControllerTestConfig extends BaseIntegrationTestConfig {
    @Autowired
    private UserSetUp userSetUp;

    private RefundEntity TEST_REFUND_ENTITY;

    @BeforeEach
    public void init() {
        UserEntity TEST_USER_ENTITY = userSetUp.saveUser();

        UserRefundRequestList requestList = new UserRefundRequestList();
        requestList.setUserRefundRequestList(List.of(
                new UserRefundRequest("TEST", "TEST", 1000, new MockMultipartFile("TEST", "TEST".getBytes())),
                new UserRefundRequest("TEST", "TEST", 1000, new MockMultipartFile("TEST", "TEST".getBytes()))
        ));

        TEST_REFUND_ENTITY = userSetUp.saveUserRefundRequest(TEST_USER_ENTITY, requestList);
    }

    @Test
    public void 환급요청평가() throws Exception {
        //when
        ResultActions resultActions = mvc.perform(multipart("/admin/refunds/{refundId}", TEST_REFUND_ENTITY.getRefundId())
                .file("barcodeImage", "TEST".getBytes())
                .param("unRefundableLineDescription", "TEST")
                .param("refundEstimateLineList[0].placeAndName", "TEST")
                .param("refundEstimateLineList[0].paymentAmount", "1000")
                .param("refundEstimateLineList[1].placeAndName", "TEST2")
                .param("refundEstimateLineList[1].paymentAmount", "2000"));

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
                                parameterWithName("unRefundableLineDescription").description("환급되지 않는 상품들에 대한 이유"),
                                parameterWithName("refundEstimateLineList[0].placeAndName").description("첫번째 환급대상 상품의 장소와 이름"),
                                parameterWithName("refundEstimateLineList[0].paymentAmount").description("첫번째 환급대상 상품의 결제금액"),
                                parameterWithName("refundEstimateLineList[1].placeAndName").description("두번째 환급대상 상품의 장소와 이름"),
                                parameterWithName("refundEstimateLineList[1].paymentAmount").description("두번째 환급대상 상품의 결제금액")
                        )
                ));
    }

}