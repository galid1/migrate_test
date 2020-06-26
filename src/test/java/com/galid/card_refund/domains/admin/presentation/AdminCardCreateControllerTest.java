package com.galid.card_refund.domains.admin.presentation;

import com.galid.card_refund.common.BaseIntegrationTest;
import com.galid.card_refund.domains.card.domain.CardInitMoney;
import com.galid.card_refund.domains.card.service.request_response.CardCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminCardCreateControllerTest extends BaseIntegrationTest {
    private String TEST_CARD_NUM = "1234123412341234";
    private CardInitMoney TEST_CARD_INIT_MONEY = CardInitMoney.TEN;

    @Test
    public void 카드생성() throws Exception {
        //given
        CardCreateRequest request = new CardCreateRequest(TEST_CARD_NUM, TEST_CARD_INIT_MONEY);

        //when
        ResultActions resultActions = mvc.perform(post("/admin/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("cardId", is(notNullValue())));

        //rest docs
        resultActions
                .andDo(document("admin/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("cardNum").description("12자리 카드번호"),
                                fieldWithPath("cardInitMoney").description("카드 초기 금액")
                        ),
                        responseFields(
                                fieldWithPath("cardId").description("Database상의 card_id")
                        )
                ));

    }

}