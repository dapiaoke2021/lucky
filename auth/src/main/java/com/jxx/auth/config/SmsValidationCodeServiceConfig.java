package com.jxx.auth.config;

import com.jxx.auth.service.IValidationCodeService;
import com.jxx.auth.service.impl.SmsValidationCodeServiceImpl;
import com.jxx.common.service.ISmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@ConditionalOnProperty(name = "validation-service", havingValue = "sms")
public class SmsValidationCodeServiceConfig {
    ISmsService smsService;
    StringRedisTemplate redisTemplate;

    @Autowired
    public SmsValidationCodeServiceConfig(ISmsService smsService, StringRedisTemplate stringRedisTemplate) {
        this.smsService = smsService;
        this.redisTemplate = stringRedisTemplate;
    }

    @Bean
    public IValidationCodeService createSmsValidationCodeService() {
        return new SmsValidationCodeServiceImpl(smsService, redisTemplate);
    }
}
