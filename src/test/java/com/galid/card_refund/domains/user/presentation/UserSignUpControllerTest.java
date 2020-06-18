package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.common.BaseIntegrationTest;
import com.galid.card_refund.domains.user.service.request_response.UserSignUpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserSignUpControllerTest extends BaseIntegrationTest {

    @Test
    public void 회원가입() throws Exception {
        //given
        UserSignUpRequest request = new UserSignUpRequest("TEST", "TEST");
        MockMultipartFile passportImage = new MockMultipartFile("test", "test".getBytes());

        //when
        ResultActions resultActions = mvc.perform(multipart("/users/auth")
                .file("image", passportImage.getBytes())
                .param("information", objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId", is(notNullValue())));

        //rest docs
        resultActions
                .andDo(document("user/{method-name}",
                    requestParameters(
                            parameterWithName("information").description("회원가입 정보 \n ex. {'deviceId': 'TEST', 'nickname': 'TEST'}")
                    ),
                    requestParts(
                            partWithName("image").description("여권 이미지")
                    ),
                    responseFields(
                            fieldWithPath("userId").description("Database상의 user_id")
                    )
                ));
    }
}