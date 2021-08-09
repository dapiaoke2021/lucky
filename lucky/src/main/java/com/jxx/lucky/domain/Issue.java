package com.jxx.lucky.domain;

import com.jxx.lucky.domain.point.PointGameBanker;
import com.jxx.lucky.param.BetParam;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public abstract class Issue {
    /**
     * 当前期号
     */
    protected String issueNo;

    /**
     * 庄家
     */
    protected Map<BankerTypeEnum, Banker> bankerMap;

    /**
     * 押注类型与最大可投注额，每次下注和撤销下注都会更新
     */
    protected Map<BetTypeEnum, Integer> topBetMap;

    /**
     * 押注类型与投注总额
     */
    protected Map<BetTypeEnum, Integer> betMap;

    /**
     * 投注期状态
     */
    protected IssueStateEnum state;

    /**
     * 参与玩家
     */
    protected Map<Long, Player> playerMap;

    public abstract void becomeBanker(BankerTypeEnum bankerType, Player player);
    public abstract void open(String[] points);
    public abstract List<BetType> getHitBets();
    public abstract void bet(Player player, List<BetParam> bets, String betNo);
    public abstract void unBet(Player player, BetTypeEnum type);
}
