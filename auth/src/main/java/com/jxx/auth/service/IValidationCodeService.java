package com.jxx.auth.service;

/**
 * @author jxx
 */
public interface IValidationCodeService {
    /**
     * 生成验证码
     * @param phone 电话号码
     * @return 验证码
     */
    void generateCode(String phone);

    /**
     * 验证
     * @param phone 电话号码
     * @param code 验证码
     * @return 成功true 失败false
     */
    boolean check(String phone, String code);
}
