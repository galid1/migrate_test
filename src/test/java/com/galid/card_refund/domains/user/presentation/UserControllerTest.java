package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.common.BaseIntegrationTest;
import com.galid.card_refund.config.UserSetUp;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserPassportStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends BaseIntegrationTest {
    @Autowired
    private UserSetUp userSetUp;

    @Test
    public void 유저정보_조회() throws Exception {
        //given
        UserEntity userEntity = userSetUp.saveUser();

        //when
        ResultActions resultActions = mvc.perform(get("/users/{userId}/information", userEntity.getUserId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("user/{method-name}",
                        responseFields(
                                fieldWithPath("nickname").description("사용자 닉네임"),
                                fieldWithPath("passportImageUrl").description("사용자 여권 이미지 경로")
                        )
                ));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("nickname").value(userEntity.getNickname()))
                .andExpect(jsonPath("passportImageUrl").value(userEntity.getPassPortImagePath()));
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
        //given
        UserEntity userEntity = userSetUp.saveUser();

        //when
        ResultActions resultActions = mvc.perform(get("/users/{userId}/passport-status", userEntity.getUserId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("user/{method-name}",
                    responseFields(
                        fieldWithPath("passportStatus").description("여권 상태 정보")
                    )
                ));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("passportStatus").value(UserPassportStatus.ESTIMATING_STATUS.toString()));
    }


}
