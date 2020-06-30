package com.galid.card_refund.common.config.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galid.card_refund.domains.user.application.request_response.UserRefundRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class UserRefundRequestFormatter implements Formatter<List<UserRefundRequest>> {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<UserRefundRequest> parse(String text, Locale locale) throws ParseException {
        UserRefundRequest[] userRefundRequests = null;

        try {
            userRefundRequests = objectMapper.readValue(text, UserRefundRequest[].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Arrays.asList(userRefundRequests);
    }

    @Override
    public String print(List<UserRefundRequest> object, Locale locale) {
        return null;
    }
}
