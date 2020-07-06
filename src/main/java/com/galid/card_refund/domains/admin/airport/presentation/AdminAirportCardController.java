package com.galid.card_refund.domains.admin.airport.presentation;

import com.galid.card_refund.domains.card.domain.CardRepository;
import com.galid.card_refund.domains.refund.domain.RefundRepository;
import com.galid.card_refund.domains.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminAirportCardController {
    private final RefundRepository refundRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;


}
