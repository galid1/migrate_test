package com.galid.card_refund.common.config.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galid.card_refund.domains.admin.presentation.request_response.RefundEstimateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RefundEstimateRequestFormatter implements Formatter<RefundEstimateRequest> {
    private final ObjectMapper objectMapper;

    @Override
    public RefundEstimateRequest parse(String text, Locale locale) throws ParseException {
        RefundEstimateRequest refundEstimateRequest = null;
        try {
            refundEstimateRequest = objectMapper.readValue(text, RefundEstimateRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return refundEstimateRequest;
    }

    @Override
    public String print(RefundEstimateRequest object, Locale locale) {
        return object.toString();
    }
}
