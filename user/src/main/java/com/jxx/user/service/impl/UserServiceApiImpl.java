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
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Service
public class UserServiceApiImpl implements IUserServiceApi {
    @Value("${lucky.robot-min-id}")
    private Long robotMinId;

    @Value("${lucky.robot-max-id}")
    private Long robotMaxId;

    @Autowired
    UserMapper userMapper;

    @Override
    public UserVO getById(Long id) {
        UserDO userDO = userMapper.selectById(id);
        UserVO userVO = new UserVO();
        userVO.setId(id);
        userVO.setUpper(userDO.getUpper());
        userVO.setMoney(userDO.getMoney());
        return userVO;
    }

    @Override
    public UserVO getRobot(Integer money) {
        UserVO userVO = getById(RandomUtil.randomLong(robotMinId, robotMaxId));
        userVO.setMoney(money);
        return userVO;
    }
}
