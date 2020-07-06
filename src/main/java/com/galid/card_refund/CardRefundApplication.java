package com.galid.card_refund;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CardRefundApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardRefundApplication.class, args);
    }

}
