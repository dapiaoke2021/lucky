package com.jxx.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jxx.user.config.UserConfig;
import com.jxx.user.dos.MoneyChangeDO;
import com.jxx.user.dos.UserDO;
import com.jxx.user.enums.MoneyChangeTypeEnum;
import com.jxx.user.mapper.MoneyChangeMapper;
import com.jxx.user.mapper.UserMapper;
import com.jxx.user.service.IUserService;
import com.jxx.user.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * @author a1
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    UserMapper userMapper;
    MoneyChangeMapper moneyChangeMapper;
    UserConfig userConfig;

    @Value("${robot-min-id}")
    private Long robotMinId;

    @Value("${robot-max-id}")
    private Long robotMaxId;


    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserConfig userConfig, MoneyChangeMapper moneyChangeMapper) {
        this.userMapper = userMapper;
        this.moneyChangeMapper = moneyChangeMapper;
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
        BigDecimal upperRebateRate = upper.getRebateRate();
        if (upperRebateRate.compareTo(userConfig.getPlatformRebateRate()) >= 0) {
            changeMoney(
                    upperId,
                    BigDecimal.valueOf(sale).multiply(userConfig.getPlatformRebateRate()).intValue(),
                    MoneyChangeTypeEnum.SALE);
            return;
        }

        BigDecimal playerRebateRate = userConfig.getPlatformRebateRate().subtract(upperRebateRate);
        changeMoney(
                upperId,
                BigDecimal.valueOf(sale).multiply(upperRebateRate).intValue(),
                MoneyChangeTypeEnum.SALE
        );
        changeMoney(
                id,
                BigDecimal.valueOf(sale).multiply(playerRebateRate).intValue(),
                MoneyChangeTypeEnum.SALE
        );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transferMoney(Long sourceId, Long targetId, Integer money) {
        UserDO source = userMapper.selectById(sourceId);
        if(source.getMoney().compareTo(money) < 0) {
            return;
        }

        changeMoney(sourceId, -money, MoneyChangeTypeEnum.TRANSFER);
        changeMoney(targetId, money, MoneyChangeTypeEnum.TRANSFER);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeMoney(Long playerId, int amount, MoneyChangeTypeEnum moneyChangeType) {
        UpdateWrapper<UserDO> userDOUpdateWrapper = new UpdateWrapper<>();
        userDOUpdateWrapper.lambda()
                .setSql("money = money + " + amount)
                .eq(UserDO::getId, playerId);
        userMapper.update(null, userDOUpdateWrapper);

        MoneyChangeDO moneyChangeDO = new MoneyChangeDO();
        moneyChangeDO.setType(moneyChangeType.getType());
        moneyChangeDO.setDescription(moneyChangeType.getDescription());
        moneyChangeDO.setAmount(amount);
        moneyChangeDO.setUserId(playerId);
        moneyChangeDO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        moneyChangeMapper.insert(moneyChangeDO);
    }

    @Override
    public void createUser(Long id) {
        UserDO userDO = new UserDO();
        userDO.setId(id);
        userMapper.insert(userDO);
    }

    @Override
    public UserVO getUser(Long id) {
        UserDO userDO = userMapper.selectById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDO, userVO);
        return userVO;
    }

    @Override
    public UserVO getRobot(Integer money) {
        UserVO userVO = new UserVO();
        userVO.setId(RandomUtil.randomLong(robotMinId, robotMaxId));
        userVO.setMoney(money);
        return userVO;
    }
}
