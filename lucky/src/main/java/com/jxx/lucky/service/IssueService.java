package com.jxx.lucky.service;

import com.jxx.lucky.domain.BankerTypeEnum;
import com.jxx.lucky.domain.Bet;
import com.jxx.lucky.domain.BetTypeEnum;

import java.util.Map;

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

    /**
     * 获取下注上限
     * @return 下注上限映射表<下注类型，上限额>
     */
    Map<BetTypeEnum, Integer> getTopBetMap();

    /**
     * 获取下注列表
     * @return 下注上限映射表<下注类型，下注总额>
     */
    Map<BetTypeEnum, Integer> getBetMap();
}
