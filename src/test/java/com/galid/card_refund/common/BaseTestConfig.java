package com.galid.card_refund.common;

import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Disabled             // 이 클래스의 테스트를 건너띔 (상속전용이므로)
@ActiveProfiles("test")
public class BaseTestConfig {
}
