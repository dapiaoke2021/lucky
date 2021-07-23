package com.jxx.auth.service.impl;

import com.alibaba.cola.exception.ExceptionFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.jxx.auth.dos.AccountDO;
import com.jxx.auth.dto.Account;
import com.jxx.auth.dto.Role;
import com.jxx.auth.mapper.AccountMapper;
import com.jxx.auth.service.IAccountService;
import com.jxx.auth.service.IValidationCodeService;
import com.jxx.auth.utils.JwtUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @author jxx
 */
@Service
@ConfigurationProperties(prefix = "role")
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountDO> implements IAccountService {

    @Setter
    @Getter
    Map<String, List<String>> authority;

    IValidationCodeService validationCodeService;
    JwtUtil jwtUtil;
    AntPathMatcher antPathMatcher;
    AccountMapper accountMapper;

    public AccountServiceImpl() {
        antPathMatcher = new AntPathMatcher();
    }

    @Autowired
    public AccountServiceImpl(IValidationCodeService validationCodeService, AccountMapper accountMapper, JwtUtil jwtUtil) {
        this();
        this.validationCodeService = validationCodeService;
        this.accountMapper = accountMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Account newAccountByUsername(String username, String password, String salt) {
        AccountDO accountDO = new AccountDO();
        accountDO.setUsername(username);
        accountDO.setPassword(password);
        accountDO.setSalt(salt);
        accountDO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        accountDO.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
        save(accountDO);

        Account account = new Account();
        BeanUtils.copyProperties(accountDO, account);
        return account;
    }

    @Override
    public Account newAccountByPhone(String phone) {
        AccountDO accountDO = new AccountDO();
        accountDO.setPhone(phone);
        accountDO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        accountDO.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
        save(accountDO);

        Account account = new Account();
        BeanUtils.copyProperties(accountDO, account);
        return account;
    }

    @Override
    public Account newAccountByDevice(String deviceId) {
        AccountDO accountDO = new AccountDO();
        accountDO.setDevice(deviceId);
        accountDO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        accountDO.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
        save(accountDO);

        Account account = new Account();
        BeanUtils.copyProperties(accountDO, account);
        return account;
    }

    @Override
    public String authByPassword(String username, String password) {
        QueryWrapper<AccountDO> queryByUsernameWrapper = new QueryWrapper<>();
        queryByUsernameWrapper.lambda()
                .eq(AccountDO::getUsername, username);
        AccountDO accountDO = accountMapper.selectOne(queryByUsernameWrapper);
        if (accountDO == null) {
            throw ExceptionFactory.bizException("未找到账号");
        }

        if (!accountDO.getPassword().equals(password)) {
            throw ExceptionFactory.bizException("密码错误");
        }

        Account account = new Account();
        BeanUtils.copyProperties(accountDO, account);
        account.setRole(createRoleByName(accountDO.getRoleName()));
        return jwtUtil.generateToken(account);
    }

    @Override
    public String authByCode(String phone, String code) {
        if(!validationCodeService.check(phone, code)) {
            throw ExceptionFactory.bizException("验证码错误");
        }

        QueryWrapper<AccountDO> queryAccountByPhoneWrapper = new QueryWrapper<>();
        queryAccountByPhoneWrapper.lambda()
                .eq(AccountDO::getPhone, phone);
        AccountDO accountDO = accountMapper.selectOne(queryAccountByPhoneWrapper);

        Account account;
        if (accountDO == null) {
            account = newAccountByPhone(phone);
        } else {
            account = new Account();
            BeanUtils.copyProperties(accountDO, account);
            if (!StringUtils.isEmpty(accountDO.getRoleName())) {
                account.setRole(createRoleByName(accountDO.getRoleName()));
            }
        }

        return jwtUtil.generateToken(account);
    }

    @Override
    public String authByDeviceId(String deviceId) {
        QueryWrapper<AccountDO> queryAccountByDeviceWrapper = new QueryWrapper<>();
        queryAccountByDeviceWrapper.lambda()
                .eq(AccountDO::getDevice, deviceId);
        AccountDO accountDO = accountMapper.selectOne(queryAccountByDeviceWrapper);;
        Account account;
        if (accountDO == null) {
            account = newAccountByDevice(deviceId);
        } else {
            account = new Account();
            BeanUtils.copyProperties(accountDO, account);
            if (!StringUtils.isEmpty(accountDO.getRoleName())) {
                account.setRole(createRoleByName(accountDO.getRoleName()));
            }
        }

        return jwtUtil.generateToken(account);
    }

    @Override
    public String getSalt() {
        return String.valueOf(System.currentTimeMillis());
    }

    @Override
    public void setRole(Long id, Long roleId) {
    }

    @Override
    public boolean checkAuthority(String role, String url) {
        List<String> authorityUrls = authority.get(role);
        if (authorityUrls == null) {
            return false;
        }
        return authorityUrls.stream().anyMatch(authorityUrl -> antPathMatcher.match(authorityUrl, url));
    }

    @Override
    public boolean save(AccountDO accountDO) {
        if (accountMapper.insert(accountDO) != 1) {
            throw ExceptionFactory.sysException("保存失败");
        }
        return true;
    }

    private Role createRoleByName(String roleName) {
        Role role = new Role(roleName);
        role.setUrls(authority.get(roleName));
        return role;
    }
}
