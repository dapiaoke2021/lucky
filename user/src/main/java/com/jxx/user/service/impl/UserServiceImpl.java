package com.jxx.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jxx.user.config.UserConfig;
import com.jxx.user.dos.UserDO;
import com.jxx.user.mapper.UserMapper;
import com.jxx.user.service.IUserService;
import com.jxx.user.service.IUserServiceApi;
import com.jxx.user.vo.UserVO;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @author a1
 */
@Service
@org.springframework.stereotype.Service
public class UserServiceImpl implements IUserServiceApi, IUserService {

    UserMapper userMapper;
    UserConfig userConfig;

    Set<Long> inGamePlayerSet;

    public UserServiceImpl(UserMapper userMapper, UserConfig userConfig) {
        this.userMapper = userMapper;
        this.userConfig = userConfig;
        inGamePlayerSet = new HashSet<>();
    }

    @Override
    public UserVO getById(Long id) {
        UserDO userDO = userMapper.selectById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDO, userVO);
        return userVO;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setUpper(Long id, Long upperId) {
        UserDO userDO = new UserDO();
        userDO.setId(id);
        userDO.setUpper(upperId);
        userMapper.updateById(userDO);
    }

    @Override
    public void increaseSale(Long id, Integer sale) {
        UserDO user = userMapper.selectById(id);
        Long upperId = user.getUpper();
        if (upperId == null) {
            return;
        }

        UserDO upper = userMapper.selectById(upperId);
        upper.getRebateRate();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transferMoney(Long sourceId, Long targetId, Integer money) {
        // 在游戏中不允许转账
        if (inGamePlayerSet.contains(sourceId)) {
            return;
        }

        UserDO source = userMapper.selectById(sourceId);
        if(source.getMoney().compareTo(money) < 0) {
            return;
        }

        UpdateWrapper<UserDO> sourceUpdateWrapper = new UpdateWrapper<>();
        sourceUpdateWrapper.lambda()
                .eq(UserDO::getId, sourceId)
                .setSql("money = money - " + money.toString());
        UpdateWrapper<UserDO> targetUpdateWrapper = new UpdateWrapper<>();
        targetUpdateWrapper.lambda()
                .eq(UserDO::getId, targetId)
                .setSql("money = money + " + money.toString());
        userMapper.update(null, sourceUpdateWrapper);
        userMapper.update(null, targetUpdateWrapper);
    }
}
