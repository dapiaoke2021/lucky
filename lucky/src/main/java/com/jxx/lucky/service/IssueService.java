package com.jxx.lucky.service;

import com.jxx.lucky.domain.BankerTypeEnum;
import com.jxx.lucky.domain.BetTypeEnum;

/**
 * @author a1
 */
public interface IssueService {
    /**
     * 下注
     * @param playerId 玩家id
     * @param money 下注额
     * @param type 押注区域
     */
    void bet(Long playerId, Integer money, BetTypeEnum type);

    /**
     * 撤销下注
     * @param playerId 玩家ID
     * @param type 押注区域
     */
    void unBet(Long playerId, BetTypeEnum type);

    /**
     * 上庄
     * @param playerId id
     * @param bankerType
     */
    void becomeBanker(Long playerId, BankerTypeEnum bankerType);

    /**
     * 开奖
     * @param point 点数
     */
    void open(Integer point);
}
