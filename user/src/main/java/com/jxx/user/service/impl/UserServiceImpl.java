package com.jxx.user.service.impl;

import com.jxx.user.service.IUserService;
import com.jxx.user.vo.UserVO;
import org.apache.dubbo.config.annotation.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Override
    public UserVO getById(Long id) {
        return null;
    }
}
