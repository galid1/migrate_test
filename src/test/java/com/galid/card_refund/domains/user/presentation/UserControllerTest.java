package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.common.BaseIntegrationTest;
import com.galid.card_refund.config.UserSetUp;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserPassportStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends BaseIntegrationTest {
    @Autowired
    private UserSetUp userSetUp;

    private UserEntity TEST_USER_ENTITY;
    private String TEST_TOKEN;

    @BeforeEach
    public void init() {
        TEST_USER_ENTITY = userSetUp.saveUser();
        TEST_TOKEN = userSetUp.signIn();
    }

    @Test
    public void 유저정보_조회() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(get("/users/{userId}/information", TEST_USER_ENTITY.getUserId())
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("nickname").value(TEST_USER_ENTITY.getNickname()))
                .andExpect(jsonPath("passportImageUrl").value(TEST_USER_ENTITY.getPassPortImagePath()));

        //restdocs
        resultActions
                .andDo(document("user/{method-name}",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer <TOKEN>")
                        ),
                        responseFields(
                                fieldWithPath("nickname").description("사용자 닉네임"),
                                fieldWithPath("passportImageUrl").description("사용자 여권 이미지 경로")
                        )
                ));
    }
    
//    @Test
//    public void 유저정보_업데이트() throws Exception {
//        //given
//        Long userId = userSetUp.saveUser(TEST_NICKNAME, TEST_PASSPORT_IMAGE_URL);
//        UserInformationUpdateRequest request = new UserInformationUpdateRequest("NEW_NICKNAME", "NEW_IMAGE".getBytes());
//
//        //when
//        ResultActions resultActions = mvc.perform(put("/users/{userId}/information", userId)
//                .param("nickname", "NEW_TEST_NICKNAME")
//                .param("image", "TEST")
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andDo(document("user/{method-name}",
//                        requestParameters(
//                                parameterWithName("nickname").description("유저의 새로운 닉네임"),
//                                parameterWithName("image").description("유저의 새로운 여권이미지")
//                        )
//                ));
//
//        //then
//        resultActions
//                .andExpect(status().isOk());
//    }

    @Test
    public void 여권상태_조회() throws Exception {
        //when
        ResultActions resultActions = mvc.perform(get("/users/{userId}/passport-status", TEST_USER_ENTITY.getUserId())
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("passportStatus").value(UserPassportStatus.ESTIMATING_STATUS.toString()));

        //restdocs
        resultActions
                .andDo(document("user/{method-name}",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer <TOKEN>")
                        ),
                        responseFields(
                                fieldWithPath("passportStatus").description("여권 상태 정보")
                        )
                ));
    }

}
