package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.common.BaseIntegrationTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserSignUpControllerTestConfig extends BaseIntegrationTestConfig {
    @Test
    public void 회원가입() throws Exception {
        //when
        ResultActions resultActions = mvc.perform(multipart("/users/auth")
                .file("passportImage", "TEST".getBytes())
                .param("deviceId", "TEST")
                .param("nickname", "TEST"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId", is(notNullValue())));

        //rest docs
        resultActions
                .andDo(document("user/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParts(
                            partWithName("passportImage").description("여권이미지")
                    ),
                    requestParameters(
                            parameterWithName("deviceId").description("유저의 mobile device id"),
                            parameterWithName("nickname").description("닉네임")
                    ),
                    responseFields(
                            fieldWithPath("userId").description("Database상의 userId")
                    )
                ));
    }
}