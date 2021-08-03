package com.jxx.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jxx.user.config.UserConfig;
import com.jxx.user.dos.UserDO;
import com.jxx.user.mapper.UserMapper;
import com.jxx.user.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author a1
 */
@Service
public class UserServiceImpl implements IUserService {

    UserMapper userMapper;
    UserConfig userConfig;


    public UserServiceImpl(UserMapper userMapper, UserConfig userConfig) {
        this.userMapper = userMapper;
        this.userConfig = userConfig;
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

    @Override
    public void changeMoney(Long playerId, int amount) {
        UpdateWrapper<UserDO> userDOUpdateWrapper = new UpdateWrapper<>();
        userDOUpdateWrapper.lambda()
                .setSql("money = money + " + amount)
                .eq(UserDO::getId, playerId);
    }
}
