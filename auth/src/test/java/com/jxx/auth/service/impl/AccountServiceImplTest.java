package com.jxx.auth.service.impl;

import com.alibaba.cola.exception.BizException;
import com.jxx.auth.dos.AccountDO;
import com.jxx.auth.dto.Account;
import com.jxx.auth.mapper.AccountMapper;
import com.jxx.auth.service.IAccountService;
import com.jxx.auth.service.IValidationCodeService;
import com.jxx.auth.utils.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.PrepareTestInstance;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class AccountServiceImplTest {
    @Mock
    AccountMapper accountMapper;

    @Mock
    IValidationCodeService validationCodeService;

    IAccountService accountService;

    JwtUtil jwtUtil = new JwtUtil();

    @BeforeEach
    public void initMock() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(accountMapper.insert(Mockito.any())).thenReturn(1);
        accountService = new AccountServiceImpl(validationCodeService, accountMapper,jwtUtil);

        ReflectionTestUtils.setField(jwtUtil, "secret", "dGhpcyBpcyBhIGV4YW1wbGU=");
        ReflectionTestUtils.setField(accountService, "jwtUtil", jwtUtil);
        ReflectionTestUtils.setField(accountService, "authority", new HashMap<String, List<String>>(){{
            put("user", new ArrayList<String>(){{
                add("/user/*");
                add("/home/*");
            }});
            put("admin", new ArrayList<String>(){{
                add("/user/*");
                add("/admin/*");
            }});
        }});

    }

    @Test
    public void testNewAccountByUserName(){
        Account account
                = accountService.newAccountByUsername("test", "testPass123", "testSalt");
        Assertions.assertEquals(account.getUsername(), "test");
        Assertions.assertEquals(account.getPassword(), "testPass123");
    }

    @Test
    public void testNewAccountByDevice() {
        Account account
                = accountService.newAccountByDevice("testDevice");
        Assertions.assertEquals(account.getDevice(), "testDevice");
    }

    @Test
    public void testNewAccountByPhone() {
        Account account
                = accountService.newAccountByPhone("13111111111");
        Assertions.assertEquals(account.getPhone(), "13111111111");
    }

    @Test
    public void testAuthByPassword() {
        AccountDO accountDO = new AccountDO();
        accountDO.setUsername("testUserName");
        accountDO.setPassword("testPassword");
        accountDO.setId(1L);

        Mockito.when(accountMapper.selectOne(Mockito.any())).thenReturn(accountDO);
        String token = accountService.authByPassword("testUserName", "testPassword");
        Account account = jwtUtil.parseToken(token);
        Assertions.assertEquals("testUserName", account.getUsername());
    }

    @Test
    public void testAuthByPasswordWhenNull() {
        Mockito.when(accountMapper.selectOne(Mockito.any())).thenReturn(null);
        Exception exception = Assertions.assertThrows(BizException.class, () -> {
            accountService.authByPassword("testUserName", "testPassword");
        });
        Assertions.assertEquals(exception.getMessage(), "未找到账号");
    }

    @Test
    public void testAuthByPasswordWhenPasswordError() {
        AccountDO accountDO = new AccountDO();
        accountDO.setUsername("testUserName");
        accountDO.setPassword("testPassword1");
        accountDO.setId(1L);
        Mockito.when(accountMapper.selectOne(Mockito.any())).thenReturn(accountDO);
        Exception exception = Assertions.assertThrows(BizException.class, () -> {
            accountService.authByPassword("testUserName", "testPassword");
        });
        Assertions.assertEquals(exception.getMessage(), "密码错误");
    }

    @Test
    public void testAuthByDevice() {
        AccountDO accountDO = new AccountDO();
        accountDO.setDevice("testDevice");
        accountDO.setId(1L);

        Mockito.when(accountMapper.selectOne(Mockito.any())).thenReturn(accountDO);
        String token = accountService.authByDeviceId("testDevice");
        Account account = jwtUtil.parseToken(token);
        Assertions.assertEquals( 1L,account.getId() );
    }

    // todo: 无法mock save函数给accountDO加上id，测试无法进行
    public void testAuthByDeviceWhenNull() {
        Mockito.when(accountMapper.selectOne(Mockito.any())).thenReturn(null);
        String token = accountService.authByDeviceId("testDevice");
        Account account = jwtUtil.parseToken(token);
        Assertions.assertEquals( 1L,account.getId() );
    }

    @Test
    public void testAuthByPhone() {
        AccountDO accountDO = new AccountDO();
        accountDO.setPhone("13111111111");
        accountDO.setId(1L);

        Mockito.when(validationCodeService.check("13111111111", "666123")).thenReturn(true);
        Mockito.when(accountMapper.selectOne(Mockito.any())).thenReturn(accountDO);

        String token = accountService.authByCode("13111111111", "666123");
        Account account = jwtUtil.parseToken(token);
        Assertions.assertEquals( 1L , account.getId());
    }

    // todo: 无法mock save函数给accountDO加上id，测试无法进行
    public void testAuthByPhoneWhenNull() {

    }
}
