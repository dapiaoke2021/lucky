package com.jxx.lucky.service;

import com.jxx.lucky.domain.Banker;
import com.jxx.lucky.domain.BankerTypeEnum;
import com.jxx.lucky.domain.BetTypeEnum;
import com.jxx.lucky.domain.Player;
import com.jxx.lucky.param.BetParam;
import com.jxx.lucky.vo.CurrentIssueDataVO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author a1
 */
public interface IssueService {
    /**
     * 下注
     * @param playerId 玩家id
     * @param bets
     * @return
     */
    String bet(Long playerId, List<BetParam> bets);

    /**
     * 撤销下注
     * @param playerId 玩家ID
     * @param type 押注区域
     */
    void unBet(Long playerId, BetTypeEnum type);

    /**
     * 上庄
     * @param playerId id
     * @param bankerType 庄家类型
     * @param money 上庄金额
     */
    void becomeBanker(Long playerId, BankerTypeEnum bankerType, Integer money);

    /**
     * 获取当前期庄家
     */
    Map<BankerTypeEnum, Banker> getCurrentBanker();

    /**
     * 获取等待中庄家
     */
    Map<BankerTypeEnum, ConcurrentLinkedQueue<Player>> getWaitBanker();


    /**
     * 下庄.
     * 如果正在上庄，则到下一句开始，自动下庄，把上庄队列中第一个玩家作为庄家。
     * 如果处于队列中，则将其从队列中移除。
     * @param playerId 玩家ID
     * @param bankerType
     */
    void offBanker(Long playerId, BankerTypeEnum bankerType);

    /**
     * 开奖
     * @param points 一分钟行情
     */
    void open(String[] points);

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

    /**
     * 获取当前期号
     */
    String getCurrentIssueNo();

    /**
     * 停止当期投注
     */
    void closeBet();

    /**
     * 获取当期期号,当期数据
     * @return
     */
    CurrentIssueDataVO currentIssueData();

}
