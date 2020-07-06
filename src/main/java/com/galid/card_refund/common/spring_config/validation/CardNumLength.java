package com.galid.card_refund.common.spring_config.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CardNumLengthValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CardNumLength {
    String message() default "CardNum은 반드시 16자리여야 합니다.";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};
}
