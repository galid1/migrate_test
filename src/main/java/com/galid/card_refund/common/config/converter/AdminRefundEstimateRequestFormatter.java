package com.galid.card_refund.common.config.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galid.card_refund.domains.admin.service.request_response.AdminRefundEstimateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AdminRefundEstimateRequestFormatter implements Formatter<AdminRefundEstimateRequest> {
    private final ObjectMapper objectMapper;

    @Override
    public AdminRefundEstimateRequest parse(String text, Locale locale) throws ParseException {
        AdminRefundEstimateRequest adminRefundEstimateRequest = null;
        try {
            adminRefundEstimateRequest = objectMapper.readValue(text, AdminRefundEstimateRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return adminRefundEstimateRequest;
    }

    @Override
    public String print(AdminRefundEstimateRequest object, Locale locale) {
        return object.toString();
    }
}
