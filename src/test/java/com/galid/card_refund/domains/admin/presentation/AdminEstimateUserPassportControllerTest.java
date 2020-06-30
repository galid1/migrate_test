package com.galid.card_refund.domains.admin.presentation;

import com.galid.card_refund.common.BaseIntegrationTest;
import com.galid.card_refund.config.UserSetUp;
import com.galid.card_refund.domains.admin.application.request_response.AdminEstimateUserPassportRequest;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserPassportStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminEstimateUserPassportControllerTest extends BaseIntegrationTest {
    @Autowired
    private UserSetUp userSetUp;

    private UserEntity TEST_USER_ENTITY;

    @BeforeEach
    public void init() {
        TEST_USER_ENTITY = userSetUp.saveUser();
    }

    @Test
    public void 여권평가() throws Exception {
        //given
        AdminEstimateUserPassportRequest request = new AdminEstimateUserPassportRequest(
                UserPassportStatus.SUCCESS_STATUS,
                "TEST",
                "TEST",
                "TEST",
                "TEST"
        );

        //when
        ResultActions resultActions = mvc.perform(post("/admin/users/{userId}/passport", TEST_USER_ENTITY.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk());

        //rest docs
        resultActions
                .andDo(document("admin/{method-name}",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("estimateResultStatus").description("평가 결과 상태"),
                                fieldWithPath("name").description("평가 결과 이름"),
                                fieldWithPath("nation").description("평가 결과 국가"),
                                fieldWithPath("passportNum").description("평가 결과 여권번호"),
                                fieldWithPath("address").description("평가 결과 주소")
                        )
                ));
    }
}