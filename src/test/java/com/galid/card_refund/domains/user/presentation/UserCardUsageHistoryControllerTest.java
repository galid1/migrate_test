package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.common.BaseIntegrationTest;
import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.config.CardSetUp;
import com.galid.card_refund.config.UserSetUp;
import com.galid.card_refund.domains.card.domain.CardEntity;
import com.galid.card_refund.domains.user.domain.UsageHistory;
import com.galid.card_refund.domains.user.domain.UserEntity;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserCardUsageHistoryControllerTest extends BaseIntegrationTest {
    @Autowired
    private UserSetUp userSetUp;
    @Autowired
    private CardSetUp cardSetUp;

    private UserEntity TEST_USER_ENTITY;
    private CardEntity TEST_CARD_ENTITY;
    private String TEST_TOKEN;

    @BeforeEach
    public void init() {
        TEST_USER_ENTITY = userSetUp.saveUser();
        TEST_CARD_ENTITY = cardSetUp.saveCard();

        userSetUp.registerCard(TEST_USER_ENTITY, TEST_CARD_ENTITY);

        TEST_TOKEN = userSetUp.signIn();
    }

    @Test
    public void 카드사용내역_조회() throws Exception {
        //given
        String TEST_PLACE = "TEST";
        Money TEST_PAYMENT = new Money(1000);
        Money TEST_REMAIN = new Money(9000);
        LocalDateTime TEST_DATE = LocalDateTime.now();

        UsageHistory history = UsageHistory.builder()
                .place(TEST_PLACE)
                .paymentAmount(TEST_PAYMENT)
                .remainAmount(TEST_REMAIN)
                .date(TEST_DATE)
                .build();
        userSetUp.saveUserCardUsageHistory(TEST_USER_ENTITY, history);


        //when
        ResultActions resultActions = mvc.perform(get("/users/{userId}/user-cards/usage", TEST_USER_ENTITY.getUserId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data", hasSize(1)))
                .andExpect(jsonPath("data[0].place").value(TEST_PLACE))
                .andExpect(jsonPath("data[0].paymentAmount").value(TEST_PAYMENT.getValue()))
                .andExpect(jsonPath("data[0].remainAmount").value(TEST_REMAIN.getValue()));

        //restdocs
        resultActions
                .andDo(document("user/{method-name}",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer <TOKEN>")
                        ),
                        responseFields(
                                fieldWithPath("data[0].date").description("카드 사용날짜"),
                                fieldWithPath("data[0].place").description("카드 사용 장소"),
                                fieldWithPath("data[0].paymentAmount").description("사용 금액"),
                                fieldWithPath("data[0].remainAmount").description("카드 잔액")
                        )
                ));
    }
}