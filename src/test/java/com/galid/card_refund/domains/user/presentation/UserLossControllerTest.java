package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.common.BaseIntegrationTest;
import com.galid.card_refund.config.CardSetUp;
import com.galid.card_refund.config.UserSetUp;
import com.galid.card_refund.domains.card.card.domain.CardEntity;
import com.galid.card_refund.domains.user.domain.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserLossControllerTest extends BaseIntegrationTest {
    @Autowired
    private UserSetUp userSetUp;
    @Autowired
    private CardSetUp cardSetUp;

    private UserEntity TEST_USER_ENTITY;
    private CardEntity TEST_CARD_ENTITY;

    @BeforeEach
    public void init() {
        TEST_USER_ENTITY = userSetUp.saveUser();
        TEST_CARD_ENTITY = cardSetUp.saveCard();
        userSetUp.registerCard(TEST_USER_ENTITY, TEST_CARD_ENTITY);
    }

    @Test
    public void 분실신고() throws Exception {
        //when
        ResultActions resultActions = mvc.perform(put("/users/{userId}/loss", TEST_USER_ENTITY.getUserId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("user/{method-name}",
                    responseFields(
                        fieldWithPath("lossId").description("Database상의 loss_id")
                    )
                ));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("lossId").isNotEmpty());
    }


}