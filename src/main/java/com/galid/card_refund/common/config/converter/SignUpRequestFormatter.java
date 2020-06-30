package com.galid.card_refund.common.config.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galid.card_refund.domains.user.application.request_response.UserSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class SignUpRequestFormatter implements Formatter<UserSignUpRequest> {
    private final ObjectMapper objectMapper;

    @Override
    public UserSignUpRequest parse(String text, Locale locale) throws ParseException {
        UserSignUpRequest request = null;
        try {
            request = objectMapper.readValue(text, UserSignUpRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    public String print(UserSignUpRequest object, Locale locale) {
        return object.toString();
    }
}
