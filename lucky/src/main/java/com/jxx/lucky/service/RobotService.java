package com.jxx.lucky.service;

import com.jxx.lucky.domain.BankerTypeEnum;
import com.jxx.lucky.domain.Player;

public interface RobotService {
    /**
     * 创建机器人，机器id（1-100）
     */
    Player createRobot();

    /**
     * 是否为机器人
     */
    boolean isRobot(Long playerId);

    /**
     * 准备下庄，随机坐庄一定期数
     */
    void prepareOff(Long playerId);

    /**
     * 处理开奖事件
     */
    void handleIssueOpenEvent();

    /**
     * 处理上庄事件
     */
    void handleBecameBankerEvent(BankerTypeEnum bankerType, Player banker);

    /**
     * 处理下庄事件
     */
    void handleOffedBankerEvent(Long playerId);
}
