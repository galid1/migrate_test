package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.common.BaseIntegrationTest;
import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.config.CardSetUp;
import com.galid.card_refund.config.UserSetUp;
import com.galid.card_refund.domains.card.domain.CardEntity;
import com.galid.card_refund.domains.card.domain.CardInformation;
import com.galid.card_refund.domains.card.domain.CardInitMoney;
import com.galid.card_refund.domains.user.domain.UsageHistory;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.service.UserCardService;
import com.galid.card_refund.domains.user.service.request_response.UserRegisterCardRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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

    @Autowired
    private UserCardService userCardService;

    private UserEntity TEST_USER_ENTITY;
    private CardEntity TEST_CARD_ENTITY;

    @BeforeEach
    public void init() {
        String TEST_DEVICE_ID = "TEST";
        String TEST_NICKNAME = "TEST";
        String TEST_PASSPORT_IMAGE = "TEST";
        TEST_USER_ENTITY = userSetUp.saveUser(TEST_DEVICE_ID, TEST_NICKNAME, TEST_PASSPORT_IMAGE);

        String TEST_CARD_NUM = "1111222211112222";
        TEST_CARD_ENTITY = cardSetUp.saveCard(new CardInformation(TEST_CARD_NUM), CardInitMoney.TEN);

        userCardService.registerCard(TEST_USER_ENTITY.getUserId(),
                new UserRegisterCardRequest(
                            TEST_CARD_ENTITY.getCardInformation().getCardNum(),
                            TEST_CARD_ENTITY.getCardInformation().getSerial()
                        ));
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
        ResultActions resultActions = mvc.perform(get("/users/{userId}/user-cards/usage", TEST_USER_ENTITY.getUserId()))
                .andDo(document("user/{method-name}",
                        responseFields(
                                fieldWithPath("data[0].date").description("카드 사용날짜"),
                                fieldWithPath("data[0].place").description("카드 사용 장소"),
                                fieldWithPath("data[0].paymentAmount").description("사용 금액"),
                                fieldWithPath("data[0].remainAmount").description("카드 잔액")
                        )
                ));


        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data", hasSize(1)))
                .andExpect(jsonPath("data[0].place").value(TEST_PLACE))
                .andExpect(jsonPath("data[0].paymentAmount").value(TEST_PAYMENT.getValue()))
                .andExpect(jsonPath("data[0].remainAmount").value(TEST_REMAIN.getValue()));
    }
}