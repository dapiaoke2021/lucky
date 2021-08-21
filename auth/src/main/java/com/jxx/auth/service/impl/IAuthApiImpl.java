package com.jxx.auth.service.impl;

import com.jxx.auth.dto.Account;
import com.jxx.auth.service.IAuthApi;
import com.jxx.auth.utils.JwtUtil;
import com.jxx.auth.vo.AccountVO;
import com.jxx.auth.vo.AuthCheckResultVO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Slf4j
public class IAuthApiImpl implements IAuthApi {

    JwtUtil jwtUtil;
    AntPathMatcher antPathMatcher;

    @Setter
    Map<String, List<String>> authority;

    @Autowired
    public IAuthApiImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        antPathMatcher = new AntPathMatcher();

    }

    @Override
    public AuthCheckResultVO checkToken(String token, String url) {
        AuthCheckResultVO authCheckResultVO = new AuthCheckResultVO();

        Account account = jwtUtil.parseToken(token);
        if (account == null) {
            authCheckResultVO.setAuthority(false);
            return authCheckResultVO;
        }
        log.debug("account = {} token ={}", account, token);
        authCheckResultVO.setAuthority(checkAuthority(account.getRole().getName(), url));
        AccountVO accountVO = new AccountVO();
        accountVO.setId(account.getId());
        accountVO.setRoleName(account.getRole().getName());
        authCheckResultVO.setAccountVO(accountVO);
        return authCheckResultVO;
    }

    private boolean checkAuthority(String role, String url) {
        List<String> authorityUrls = authority.get(role);
        if (authorityUrls == null) {
            return false;
        }
        return authorityUrls.stream().anyMatch(authorityUrl -> antPathMatcher.match(authorityUrl, url));
    }
}
