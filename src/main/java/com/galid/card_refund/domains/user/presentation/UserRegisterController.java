package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.service.UserRegisterService;
import com.galid.card_refund.domains.user.service.request_response.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserRegisterController {
    private final UserRegisterService userRegisterService;

    @PostMapping("/users")
    public long registerUser(@RequestBody @Valid UserRegisterRequest request) throws IOException {
        return userRegisterService.registerUser(request);
    }

}
