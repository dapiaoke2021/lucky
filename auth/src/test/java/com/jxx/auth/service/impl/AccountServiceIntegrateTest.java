package com.jxx.auth.service.impl;

import com.jxx.auth.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

@SpringBootTest(classes = AccountServiceImpl.class)
public class AccountServiceIntegrateTest {

    @Autowired
    AccountServiceImpl accountService;

    @Test
    public void testAuthroiryInjection() {
        Map<String, List<String>> authority = accountService.getAuthority();
        Assertions.assertNotNull(authority);
    }
}
