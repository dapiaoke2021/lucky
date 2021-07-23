package com.jxx.auth.service.impl;

import com.alibaba.cola.exception.ExceptionFactory;
import com.jxx.auth.service.IValidationCodeService;
import com.jxx.common.service.ISmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author a1
 */
@Service
public class ValidationCodeServiceImpl implements IValidationCodeService {

    @Autowired
    ISmsService smsService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Value("${sms.tencent.templateId}")
    private String templateId;

    @Override
    public void generateCode(String phone) {
        int code = (int)(Math.random() * 999999);
        String codeStr = String.format("%06d", code);
        redisTemplate.opsForValue().set("code:" + phone, codeStr, 60, TimeUnit.SECONDS);
        smsService.sendSms(phone, templateId, new String[]{codeStr});
    }

    @Override
    public boolean check(String phone, String code) {
        String cachedCode = redisTemplate.opsForValue().get(phone);
        if (StringUtils.isEmpty(cachedCode)) {
            throw ExceptionFactory.bizException("短信验证码过期");
        }

        if(code.equals(cachedCode)) {
            redisTemplate.delete(phone);
            return true;
        } else {
            return false;
        }
    }
}
