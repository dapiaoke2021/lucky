package com.jxx.auth.service;

import com.jxx.auth.vo.AccountVO;
import com.jxx.auth.vo.AuthCheckResultVO;

public interface IAuthApi {
    /**
     * 检查token权限
     * @param token token
     * @param url 访问url
     * @return 通过则返回账号信息，否则抛出异常
     */
    AuthCheckResultVO checkToken(String token, String url);
}
