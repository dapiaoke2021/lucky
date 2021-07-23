package com.jxx.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jxx.auth.dos.AccountDO;
import com.jxx.auth.dto.Account;

/**
 * @author a1
 */
public interface IAccountService extends IService<AccountDO> {
    /**
     * 账号密码创建账号
     * @param username 账号
     * @param password 密码（MD5(password + salt)）
     * @param salt 用于密码md5加密
     * @return 新账号
     */
    Account newAccountByUsername(String username, String password, String salt);

    /**
     * 通过电话号码创建账号
     * @param phone 电话号码
     * @return 账号
     */
    Account newAccountByPhone(String phone);

    /**
     * 通过设备号创建账号
     * @param deviceId 设备号
     * @return 账号
     */
    Account newAccountByDevice(String deviceId);

    /**
     * 通过密码认证
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    String authByPassword(String username, String password);

    /**
     * 通过电话验证码认证
     * @param phone 电话
     * @param code 验证码
     * @return token
     */
    String authByCode(String phone, String code);

    /**
     * 通过设备ID认证
     * @param deviceId 设备ID
     * @return token
     */
    String authByDeviceId(String deviceId);

    /**
     * 获取加密随机字符串
     * @return 随机字符串
     */
    String getSalt();

    /**
     * 设置账号角色
     * @param id 账号ID
     * @param roleId 角色ID
     */
    void setRole(Long id, Long roleId);


    /**
     * 检查权限
     * @param role 角色ID
     * @param url 资源ID
     * @return 鉴权结果
     */
    boolean checkAuthority(String role, String url);
}
