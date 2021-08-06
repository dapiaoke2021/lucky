package com.jxx.auth.config;

import com.jxx.auth.service.IValidationCodeService;
import com.jxx.auth.service.impl.FakeValidationCodeServiceImpl;
import com.jxx.auth.service.impl.SmsValidationCodeServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "validation-service", havingValue = "fake")
public class FakeValidationCodeServiceConfig {

    @Bean
    public IValidationCodeService createFakeValidationCodeService() {
        return new FakeValidationCodeServiceImpl();
    }
}
