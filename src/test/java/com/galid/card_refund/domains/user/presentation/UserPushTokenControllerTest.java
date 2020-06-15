package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.common.BaseIntegrationTest;
import com.galid.card_refund.config.UserSetUp;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.service.request_response.StorePushTokenRequest;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserPushTokenControllerTest extends BaseIntegrationTest {
    @Autowired
    private UserSetUp userSetUp;

    private UserEntity TEST_USER_ENTITY;
    private String TEST_PUSH_TOKEN;
    private String API_TOKEN;

    @BeforeEach
    public void init() {
        TEST_USER_ENTITY = userSetUp.saveUser();
        TEST_PUSH_TOKEN = "TEST";
        API_TOKEN = userSetUp.signIn();
    }

    @Test
    public void 푸시토큰_저장() throws Exception {
        //given
        StorePushTokenRequest request = new StorePushTokenRequest(TEST_PUSH_TOKEN);

        //when
        ResultActions resultActions = mvc.perform(post("/users/{userId}/push-token", TEST_USER_ENTITY.getUserId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + API_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk());

        //rest docs
        resultActions
                .andDo(document("user/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                            fieldWithPath("pushToken").description("Push 알림을 위한 DeviceToken")
                        )
                ));
    }

    @Test
    public void 푸시토큰_갱신() throws Exception{
        //given
        //when
        //then
        //rest docs
    }

}