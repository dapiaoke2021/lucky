package com.jxx.auth.service.impl;

import com.jxx.auth.service.IValidationCodeService;

public class FakeValidationCodeServiceImpl implements IValidationCodeService {
    @Override
    public void generateCode(String phone) {
        // dummy
    }

    @Override
    public boolean check(String phone, String code) {
        return code.equals("666666");
    }
}
