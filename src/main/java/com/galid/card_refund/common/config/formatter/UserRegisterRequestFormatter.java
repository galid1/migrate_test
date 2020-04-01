package com.galid.card_refund.common.config.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galid.card_refund.domains.user.service.request_response.UserRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class UserRegisterRequestFormatter implements Formatter<UserRegisterRequest> {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public UserRegisterRequest parse(String text, Locale locale) throws ParseException {
        UserRegisterRequest request = null;

        try {
            request = mapper.readValue(text, UserRegisterRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return request;
    }

    @Override
    public String print(UserRegisterRequest object, Locale locale) {
        return object.toString();
    }
}
