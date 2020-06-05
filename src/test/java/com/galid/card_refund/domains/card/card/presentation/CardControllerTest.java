package com.galid.card_refund.domains.card.card.presentation;

import com.galid.card_refund.common.BaseIntegrationTest;
import com.galid.card_refund.domains.card.card.domain.CardInitMoney;
import com.galid.card_refund.domains.card.card.service.CardService;
import com.galid.card_refund.domains.card.card.service.request_response.CardCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CardControllerTest extends BaseIntegrationTest {
    @Autowired
    private CardService cardService;

    @Test
    public void 카드생성() throws Exception {
        //given
        CardCreateRequest createRequest = new CardCreateRequest("1234123412341234", CardInitMoney.TEN);

        //when
        ResultActions resultActions = mvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andDo(document("card/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("cardNum").description("16자리 카드번호"),
                                fieldWithPath("cardInitMoney").description("초기 카드 금액")
                        ),
                        responseFields(
                                fieldWithPath("cardId").description("디비상의 card_id")
                        )
                ));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("cardId", is(notNullValue())));
    }

}