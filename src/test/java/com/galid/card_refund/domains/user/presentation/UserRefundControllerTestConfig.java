package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.common.BaseIntegrationTestConfig;
import com.galid.card_refund.config.CardSetUp;
import com.galid.card_refund.config.UserSetUp;
import com.galid.card_refund.domains.card.domain.CardEntity;
import com.galid.card_refund.domains.refund.domain.RefundEntity;
import com.galid.card_refund.domains.user.application.request_response.UserRefundRequestList;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.application.request_response.UserRefundRequest;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRefundControllerTestConfig extends BaseIntegrationTestConfig {
    @Autowired
    private UserSetUp userSetUp;
    @Autowired
    private CardSetUp cardSetUp;

    private UserEntity TEST_USER_ENTITY;
    private CardEntity TEST_CARD_ENTITY;
    private RefundEntity TEST_REFUND_ENTITY;
    private String TEST_TOKEN;

    @BeforeEach
    public void init() {
        TEST_USER_ENTITY = userSetUp.saveUser();
        TEST_CARD_ENTITY = cardSetUp.saveCard();
        userSetUp.registerCard(TEST_USER_ENTITY, TEST_CARD_ENTITY);
        userSetUp.estimateUserPassport(TEST_USER_ENTITY);
        TEST_TOKEN = userSetUp.signIn();
    }

    @Test
    public void 환급요청() throws Exception {
        //when
        ResultActions resultActions = mvc.perform(multipart("/users/{userId}/refunds", TEST_USER_ENTITY.getUserId())
                .file("userRefundRequestList[0].refundItemImage", "TEST".getBytes())
                .file("userRefundRequestList[1].refundItemImage", "TEST".getBytes())
                .param("userRefundRequestList[0].place", "TEST")
                .param("userRefundRequestList[0].purchaseDateTime", "TEST")
                .param("userRefundRequestList[0].paymentAmount", String.valueOf(1000))
                .param("userRefundRequestList[1].place", "TEST")
                .param("userRefundRequestList[1].purchaseDateTime", "TEST")
                .param("userRefundRequestList[1].paymentAmount", String.valueOf(1000))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN)
        );

        //then
        double expectRefundAmount = 2000 * 1 / 11;

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("refundId", is(notNullValue())))
                .andExpect(jsonPath("expectRefundAmount").value(expectRefundAmount));

        //rest docs
        resultActions
                .andDo(document("user/{method-name}",
                        requestParts(
                                partWithName("userRefundRequestList[0].refundItemImage").description("첫번째 환급 물품의 이미지"),
                                partWithName("userRefundRequestList[1].refundItemImage").description("두번째 환급 물품의 이미지 (2개 이상의 환급 물품이 존재한다면, 임의의 key값과 함께, 같은 방식으로 이미지를 추가하면됨.")
                        ),
                        requestParameters(
                                parameterWithName("userRefundRequestList[0].place").description("첫번째 환급 물품의 구매 장소"),
                                parameterWithName("userRefundRequestList[0].purchaseDateTime").description("첫번째 환급 물품의 구매 날짜"),
                                parameterWithName("userRefundRequestList[0].paymentAmount").description("첫번째 환급 물품의 결제액"),
                                parameterWithName("userRefundRequestList[1].place").description("두번째 환급 물품의 구매 장소"),
                                parameterWithName("userRefundRequestList[1].purchaseDateTime").description("두번째 환급 물품의 구매 날짜"),
                                parameterWithName("userRefundRequestList[1].paymentAmount").description("두번째 환급 물품의 결제액")
                        ),
                        responseFields(
                                fieldWithPath("refundId").description("Database상의 refund_id"),
                                fieldWithPath("expectRefundAmount").description("환급 예상 금액")
                        )
                ));
    }

    @Test
    public void 환급요청_결과_조회() throws Exception {
        //given
        TEST_REFUND_ENTITY = userSetUp.saveUserRefundRequest(TEST_USER_ENTITY, makeTestUserRefundRequestList());
        userSetUp.estimateRefundRequest(TEST_REFUND_ENTITY.getRefundId());

        //when
        ResultActions resultActions = mvc.perform(get("/users/{userId}/refunds", TEST_USER_ENTITY.getUserId())
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN));

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

        //restdocs
        resultActions
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
    }


    private UserRefundRequestList makeTestUserRefundRequestList() {
        UserRefundRequestList userRefundRequestList = new UserRefundRequestList();
        userRefundRequestList.setUserRefundRequestList(List.of(
                new UserRefundRequest("TEST",
                            "TEST",
                              1000,
                                             new MockMultipartFile("TEST", "TEST".getBytes()))
        ));
        return userRefundRequestList;
    }
}