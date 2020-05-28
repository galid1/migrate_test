package com.galid.card_refund.domains.card.presentation;

import com.galid.card_refund.common.BaseIntegrationTest;
import com.galid.card_refund.domains.card.domain.CardInitMoney;
import com.galid.card_refund.domains.card.service.CardService;
import com.galid.card_refund.domains.card.service.request_response.CardCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CardControllerTest extends BaseIntegrationTest {
    @Autowired
    private CardService cardService;

    @Test
    public void 카드생성() throws Exception {
        //given
        CardCreateRequest createRequest = new CardCreateRequest("1234123422223333", CardInitMoney.TEN);

        //when, then
        mvc.perform(post("/cards")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("cardId", is(notNullValue())))
                .andDo(print())
                .andDo(document("card/{method-name}",
                        requestFields(
                                fieldWithPath("cardNum").description("16자리 카드번호"),
                                fieldWithPath("cardInitMoney").description("초기 카드 금액")
                        ),
                        responseFields(
                                fieldWithPath("cardId").description("디비상의 card_id")
                        )
                ));
    }
}