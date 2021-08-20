package com.jxx.auth.service.impl;

import com.jxx.auth.service.IValidationCodeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeValidationCodeServiceImpl implements IValidationCodeService {
    @Override
    public void generateCode(String phone) {
        // dummy
    }

    @Override
    public boolean check(String phone, String code) {
        log.debug("phone={} code={}", phone, code);
        return code.equals("666666");
    }
}
