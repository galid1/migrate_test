package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.common.BaseIntegrationTest;
import com.galid.card_refund.config.CardSetUp;
import com.galid.card_refund.config.UserSetUp;
import com.galid.card_refund.domains.card.domain.CardEntity;
import com.galid.card_refund.domains.card.domain.CardInitMoney;
import com.galid.card_refund.domains.card.domain.CardStatus;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.service.UserCardService;
import com.galid.card_refund.domains.user.service.request_response.UserRegisterCardRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;


import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserCardControllerTest  extends BaseIntegrationTest {
    @Autowired
    private UserSetUp userSetUp;
    @Autowired
    private CardSetUp cardSetUp;
    @Autowired
    private UserCardService userCardService;

    private CardEntity TEST_CARD_ENTITY;
    private UserEntity TEST_USER_ENTITY;

    private String TEST_TOKEN;

    @BeforeEach
    public void init() {
        TEST_USER_ENTITY = userSetUp.saveUser();
        TEST_TOKEN = userSetUp.signIn();
        TEST_CARD_ENTITY = cardSetUp.saveCard();
    }

    @Test
    public void 유저_카드등록() throws Exception {
        //given
        UserRegisterCardRequest request = new UserRegisterCardRequest(
                TEST_CARD_ENTITY.getCardInformation().getCardNum(),
                TEST_CARD_ENTITY.getCardInformation().getSerial());

        //when
        ResultActions resultActions = mvc.perform(post("/users/{userId}/user-cards", TEST_USER_ENTITY.getUserId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("cardId", is(notNullValue())));
        assertEquals(TEST_CARD_ENTITY.getCardStatus(), CardStatus.REGISTERED_STATUS);

        //rest docs
        resultActions
                .andDo(document("user/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer <TOKEN>")
                        ),
                        requestFields(
                                fieldWithPath("cardNum").description("등록할 12자리 카드번호"),
                                fieldWithPath("serial").description("등록할 카드의 4자리 serial 번호")
                        ),
                        responseFields(
                                fieldWithPath("cardId").description("Database 상의 card_id")
                        )
                ));
    }

    @Test
    public void 유저_카드반납() throws Exception {
        //given
        userSetUp.registerCard(TEST_USER_ENTITY, TEST_CARD_ENTITY);

        //when
        ResultActions resultActions = mvc.perform(put("/users/{userId}/user-cards", TEST_USER_ENTITY.getUserId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN));

        //then
        resultActions
                .andExpect(status().isOk());
        assertEquals(TEST_CARD_ENTITY.getCardStatus(), CardStatus.RETURNED_STATUS);

        //restdocs
        resultActions
                .andDo(document("user/{method-name}",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer <TOKEN>")
                        )
                ));
    }

    @Test
    public void 유저_카드등록상태_조회() throws Exception {
        //given
        userSetUp.registerCard(TEST_USER_ENTITY, TEST_CARD_ENTITY);

        //when
        ResultActions resultActions = mvc.perform(get("/users/{userId}/user-cards", TEST_USER_ENTITY.getUserId())
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("ownerId").value(TEST_USER_ENTITY.getUserId()))
                .andExpect(jsonPath("remainAmount").value(CardInitMoney.TEN.getAmount().getValue()));

        //restdocs
        resultActions
                .andDo(document("user/{method-name}",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer <TOKEN>")
                        ),
                        responseFields(
                                fieldWithPath("ownerId").description("Database 상의 카드 소유주 user_id"),
                                fieldWithPath("remainAmount").description("카드 잔액")
                        )
                ));
    }

}