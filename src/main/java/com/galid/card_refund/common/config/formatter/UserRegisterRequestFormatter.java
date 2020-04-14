package com.galid.card_refund.common.config.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galid.card_refund.domains.user.service.request_response.UserSignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class UserRegisterRequestFormatter implements Formatter<UserSignUpRequest> {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public UserSignUpRequest parse(String text, Locale locale) throws ParseException {
        UserSignUpRequest request = null;

        try {
            request = mapper.readValue(text, UserSignUpRequest.class);
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
