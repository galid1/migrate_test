package com.galid.card_refund.domains.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galid.card_refund.domains.user.service.request_response.UserRegisterRequest;
import com.galid.card_refund.domains.user.service.UserRegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AutoConfigureMockMvc
@WebMvcTest
@MockBean(JpaMetamodelMappingContext.class)
public class UserRegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserRegisterService registerService;

    @Test
    public void registerUserTest() throws Exception {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .deviceId("1111")
                .nickname("asd")
                .build();

        when(registerService.registerUser(request))
                .thenReturn(1l);

        this.mockMvc
                .perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}