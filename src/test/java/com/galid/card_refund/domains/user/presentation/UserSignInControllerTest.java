package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.common.BaseIntegrationTest;
import com.galid.card_refund.config.UserSetUp;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.service.request_response.UserSignInRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserSignInControllerTest extends BaseIntegrationTest {
    @Autowired
    private UserSetUp userSetUp;

    @Test
    public void 로그인() throws Exception {
        //given
        UserEntity userEntity = userSetUp.saveUser();
        UserSignInRequest signInRequest = new UserSignInRequest(userEntity.getDeviceId());

        //when
        ResultActions resultActions = mvc.perform(post("/users/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(document("user/{method-name}",
                        requestFields(
                                fieldWithPath("deviceId").description("디바이스 고유 id")
                        ),
                        responseFields(
                                fieldWithPath("userId").description("Database상의 user_id")
                        )
                ));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId", is(notNullValue())));
    }
}