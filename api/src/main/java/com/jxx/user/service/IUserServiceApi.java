package com.jxx.user.service;

import com.jxx.user.vo.UserVO;

/**
 * @author a1
 */
public interface IUserServiceApi {
    /**
     * 获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    UserVO getById(Long id);
}
