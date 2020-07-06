package com.galid.card_refund.common.spring_config.validation;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class CardNumLengthValidator implements ConstraintValidator<CardNumLength, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.length() == 16;
    }
}
