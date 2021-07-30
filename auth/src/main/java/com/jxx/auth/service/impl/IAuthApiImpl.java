package com.jxx.auth.service.impl;

import com.jxx.auth.dto.Account;
import com.jxx.auth.service.IAccountService;
import com.jxx.auth.service.IAuthApi;
import com.jxx.auth.utils.JwtUtil;
import com.jxx.auth.vo.AccountVO;
import com.jxx.auth.vo.AuthCheckResultVO;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service
public class IAuthApiImpl implements IAuthApi {
    IAccountService accountService;

    JwtUtil jwtUtil;

    @Autowired
    public IAuthApiImpl(IAccountService accountService, JwtUtil jwtUtil) {
        this.accountService = accountService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthCheckResultVO checkToken(String token, String url) {
        AuthCheckResultVO authCheckResultVO = new AuthCheckResultVO();

        Account account = jwtUtil.parseToken(token);
        if (account == null) {
            authCheckResultVO.setAuthority(false);
            return authCheckResultVO;
        }
        authCheckResultVO.setAuthority(accountService.checkAuthority(account.getRole().getName(), url));
        AccountVO accountVO = new AccountVO();
        accountVO.setId(account.getId());
        accountVO.setRoleName(account.getRole().getName());
        authCheckResultVO.setAccountVO(accountVO);
        return authCheckResultVO;
    }
}
