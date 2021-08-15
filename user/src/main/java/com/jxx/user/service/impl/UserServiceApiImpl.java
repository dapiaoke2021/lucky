package com.jxx.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.jxx.user.dos.UserDO;
import com.jxx.user.mapper.UserMapper;
import com.jxx.user.service.IUserService;
import com.jxx.user.service.IUserServiceApi;
import com.jxx.user.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Service
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
