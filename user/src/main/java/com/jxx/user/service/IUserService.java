package com.jxx.user.service;

import com.jxx.user.enums.MoneyChangeTypeEnum;
import com.jxx.user.vo.UserVO;

/**
 * @author a1
 */
public interface IUserService {
    /**
     * 设置上级
     * @param id 用户ID
     * @param upperId 上级id
     */
    void setUpper(Long id, Long upperId);

    /**
     * 增加业绩，规则进行佣金分配
     * @param id 用户ID
     * @param sale 业绩
     */
    void increaseSale(Long id, Integer sale);

    /**
     * 转账
     * @param sourceId 来源用户ID
     * @param targetId 目标用户ID
     * @param money 金额
     */
    void transferMoney(Long sourceId, Long targetId, Integer money);

    /**
     * 更新资产
     */
    void changeMoney(Long id, int amount, MoneyChangeTypeEnum moneyChangeType);


    /**
     * 创建用户
     */
    void createUser(Long id);

    /**
     * 获取用户信息
     */
    UserVO getUser(Long id);
}
