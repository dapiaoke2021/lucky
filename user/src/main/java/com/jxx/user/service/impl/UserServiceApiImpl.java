package com.jxx.user.service.impl;

import com.jxx.user.service.IUserService;
import com.jxx.user.service.IUserServiceApi;
import com.jxx.user.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RefreshScope
public class UserServiceApiImpl implements IUserServiceApi {

    @Autowired
    IUserService userService;

    @Override
    public UserVO getById(Long id) {
        return userService.getUser(id);
    }

    @Override
    public UserVO getRobot(Integer money) {
        return userService.getRobot(money);
    }
}
