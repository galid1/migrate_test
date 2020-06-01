package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.common.BaseIntegrationTest;
import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.config.CardSetUp;
import com.galid.card_refund.config.UserSetUp;
import com.galid.card_refund.domains.card.domain.CardEntity;
import com.galid.card_refund.domains.refund.domain.RefundEntity;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.service.request_response.UserRefundRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRefundControllerTest extends BaseIntegrationTest {
    @Autowired
    private UserSetUp userSetUp;
    @Autowired
    private CardSetUp cardSetUp;

    private UserEntity TEST_USER_ENTITY;
    private CardEntity TEST_CARD_ENTITY;
    private RefundEntity TEST_REFUND_ENTITY;

    @BeforeEach
    public void init() {
        TEST_USER_ENTITY = userSetUp.saveUser();
        TEST_CARD_ENTITY = cardSetUp.saveCard();
        userSetUp.registerCard(TEST_USER_ENTITY, TEST_CARD_ENTITY);
        userSetUp.estimateUserPassport(TEST_USER_ENTITY);
    }

    @Test
    public void 환급요청_결과_조회() throws Exception {
        //given
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
        userSetUp.estimateRefundRequest(TEST_REFUND_ENTITY.getRefundId());

        //when
        ResultActions resultActions = mvc.perform(get("/users/{userId}/refunds", TEST_USER_ENTITY.getUserId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(document("user/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("userInformation.name").description("유저의 이름(여권 검증을 통해 저장된)"),
                                fieldWithPath("userInformation.nation").description("유저의 국가(여권 검증을 통해 저장된)"),
                                fieldWithPath("userInformation.address").description("유저의 주소(여권 검증을 통해 저장된)"),
                                fieldWithPath("userInformation.passportNum").description("여권 번호"),

                                fieldWithPath("refundResultResponseLineList[0].place").description("환급처리 결과 대상 상품 구매 장소"),
                                fieldWithPath("refundResultResponseLineList[0].paymentAmount").description("환급처리 결과 대상 상품 지불 금액"),
                                fieldWithPath("refundResultResponseLineList[0].refundAmount").description("환급처리 결과 대상 상품 환급 금액"),

                                fieldWithPath("unRefundableLineDescription").description("환급 처리 불가능한 상품 이유 설명"),
                                fieldWithPath("refundResultBarcodeImageUrl").description("환급 요청 결과 화면 바코드 이미지")
                        )
                ));


        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("userInformation.name").value(TEST_USER_ENTITY.getUserPassportInformation().getName()))
                .andExpect(jsonPath("userInformation.nation").value(TEST_USER_ENTITY.getUserPassportInformation().getNation()))
                .andExpect(jsonPath("userInformation.address").value(TEST_USER_ENTITY.getUserPassportInformation().getAddress()))
                .andExpect(jsonPath("userInformation.passportNum").value(TEST_USER_ENTITY.getUserPassportInformation().getPassportNum()))

                .andExpect(jsonPath("refundResultResponseLineList[0].place").value(TEST_REFUND_ENTITY.getRefundResultLineList()
                        .get(0)
                        .getPlace()))
                .andExpect(jsonPath("refundResultResponseLineList[0].paymentAmount").value(TEST_REFUND_ENTITY.getRefundResultLineList()
                        .get(0)
                        .getPaymentAmount()
                        .getValue()))
                .andExpect(jsonPath("refundResultResponseLineList[0].refundAmount").value(TEST_REFUND_ENTITY.getRefundResultLineList()
                        .get(0)
                        .getRefundAmount()
                        .getValue()))

                .andExpect(jsonPath("unRefundableLineDescription").value(TEST_REFUND_ENTITY.getUnRefundableLineDescription()))
                .andExpect(jsonPath("refundResultBarcodeImageUrl").value(TEST_REFUND_ENTITY.getRefundResultBarcodeImageUrl()));
    }
}