package com.jxx.auth.controller;

import com.alibaba.cola.dto.SingleResponse;
import com.jxx.auth.dto.Account;
import com.jxx.auth.dto.SignPart;
import com.jxx.auth.param.AuthorityByMd5Param;
import com.jxx.auth.service.IAccountService;
import com.jxx.auth.service.IThirdPartyService;
import com.jxx.auth.service.IValidationCodeService;
import com.jxx.auth.vo.AccountVO;
import com.jxx.common.aop.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.alibaba.cola.dto.Response;

import java.util.List;

/**
 * @author a1
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    IThirdPartyService thirdPartyService;

    @Autowired
    IAccountService accountService;

    @Autowired
    IValidationCodeService validationCodeService;

    @GetMapping("/authority/md5")
    public Response authority(AuthorityByMd5Param authorityByMd5Param, @RequestBody List<SignPart> signParts) {
        boolean success = thirdPartyService.authorityByMd5(
                authorityByMd5Param.getMerchantId(),
                authorityByMd5Param.getSign(),
                signParts);
        if (success) {
            return Response.buildSuccess();
        } else {
            return Response.buildFailure("ERR_SIGN", "验签名失败");
        }
    }

    @PostMapping
    public Response newAccount(String username, String password, String salt) {
        accountService.newAccountByUsername(username, password, salt);
        return Response.buildSuccess();
    }

    @GetMapping("/password")
    public SingleResponse<String> authByPassword(String username, String pwd) {
        log.info("password收到请求 username={} pwd={}", username, pwd);
        return SingleResponse.of(accountService.authByPassword(username, pwd));
    }

    @GetMapping("/device")
    public SingleResponse<String> authByDevice(String device) {
        return SingleResponse.of(accountService.authByDeviceId(device));
    }

    @GetMapping("/code")
    public SingleResponse<String> authByCode(String phone, String code) {
        if (StringUtils.isEmpty(code)) {
            validationCodeService.generateCode(phone);
            return SingleResponse.buildSuccess();
        }

        return SingleResponse.of(accountService.authByCode(phone, code));
    }


    @UserId
    @GetMapping("/accountById")
    public SingleResponse<AccountVO> accountById(Long userId) {
        log.debug("userId={}", userId);
        return SingleResponse.of(accountService.accountById(userId));
    }
}
