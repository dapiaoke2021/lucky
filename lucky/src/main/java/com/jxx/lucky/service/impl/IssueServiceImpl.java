package com.jxx.lucky.service.impl;

import com.jxx.lucky.domain.BetTypeEnum;
import com.jxx.lucky.domain.Issue;
import com.jxx.lucky.domain.Player;
import com.jxx.lucky.service.IssueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author a1
 */
@Service
public class IssueServiceImpl implements IssueService {

    private Issue currentIssue;

    @Override
    public void bet(Long playerId, Integer money, BetTypeEnum type) {
        // 没有的话从，数据库中加载
        Player player = getPlayer(playerId);
        player.bet(money, type);
        currentIssue.bet(player, money, type);
        // 下注成功保存数据库

    }

    @Override
    public void unBet(Long playerId, BetTypeEnum type) {
        Player player = getPlayer(playerId);
        currentIssue.unBet(player, type);
    }

    @Override
    public void becomeBanker(Long playerId) {
        // 保存庄家数据
    }

    @Override
    public void open(Integer point) {
        currentIssue.open(point);
        saveIssue(currentIssue);
        currentIssue = new Issue();
    }

    @Transactional
    protected void saveIssue(Issue currentIssue) {
        // 如果保存错误，在异常处理中将所有投注记录设置为失效（INIT，OPENED, FAIL）
    }

    /**
     * 先从当前期中查找，不存在的话，从数据库加载
     * @param id 玩家id
     * @return 玩家对象
     */
    private Player getPlayer(Long id) {
        return null;
    }
}
