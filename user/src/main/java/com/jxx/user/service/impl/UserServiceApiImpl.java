package com.jxx.user.service.impl;

import com.jxx.user.dos.UserDO;
import com.jxx.user.mapper.UserMapper;
import com.jxx.user.service.IUserServiceApi;
import com.jxx.user.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Service
public class UserServiceApiImpl implements IUserServiceApi {

    public UserServiceApiImpl() {
        log.info("UserServiceApiImpl init");
    }

    @Override
    public UserVO getById(Long id) {
        UserVO userVO = new UserVO();
        return userVO;
    }
}
