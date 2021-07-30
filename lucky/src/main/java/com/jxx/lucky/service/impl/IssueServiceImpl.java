package com.jxx.lucky.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jxx.lucky.domain.*;
import com.jxx.lucky.dos.BankerRecordDO;
import com.jxx.lucky.dos.BetRecordDO;
import com.jxx.lucky.dos.IssueDO;
import com.jxx.lucky.mapper.BetMapper;
import com.jxx.lucky.mapper.IssueMapper;
import com.jxx.lucky.service.IssueService;
import com.jxx.user.vo.UserVO;
import com.jxx.user.service.IUserServiceApi;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;

/**
 * @author a1
 */
@Service
public class IssueServiceImpl implements IssueService {

    private Issue currentIssue;

    @Reference
    IUserServiceApi userService;

    @Autowired
    BetMapper betMapper;

    @Autowired
    IssueMapper issueMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bet(Long playerId, Integer money, BetTypeEnum type) {
        Player player = getPlayer(playerId);

        // 下注成功保存数据库
        BetRecordDO betRecordDO = new BetRecordDO();
        betRecordDO.setBetType(type);
        betRecordDO.setIssueNo(currentIssue.getIssueNo());
        betRecordDO.setMoney(money);
        betRecordDO.setPlayerId(playerId);
        betRecordDO.setState(BetStateEnum.ONGOING);
        betMapper.insert(betRecordDO);

        currentIssue.bet(player, money, type);
    }

    @Override
    public void unBet(Long playerId, BetTypeEnum type) {
        Player player = getPlayer(playerId);

        UpdateWrapper<BetRecordDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(BetRecordDO::getPlayerId, player)
                .eq(BetRecordDO::getIssueNo, currentIssue.getIssueNo())
                .eq(BetRecordDO::getBetType, type)
                .set(BetRecordDO::getState, BetStateEnum.REVOKE);
        currentIssue.unBet(player, type);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void becomeBanker(Long playerId, BankerTypeEnum bankerType) {
        // 保存庄家数据
        UpdateWrapper<IssueDO> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<IssueDO> lambdaUpdateWrapper = updateWrapper.lambda();
        switch (bankerType) {
            case BIG_SMALL:
                lambdaUpdateWrapper.set(IssueDO::getSmallBigBanker, playerId);
                break;
            case OOD_EVEN:
                lambdaUpdateWrapper.set(IssueDO::getOodEvenBanker, playerId);
                break;
            case NUMBER:
                lambdaUpdateWrapper.set(IssueDO::getNumberBanker, playerId);
                break;
            default:
                assert false;
                break;
        }
        lambdaUpdateWrapper.eq(IssueDO::getIssueNo, currentIssue.getIssueNo());
        issueMapper.update(null, updateWrapper);
        currentIssue.becomeBanker(bankerType, getPlayer(playerId));
    }

    @Override
    public void open(Integer point) {
        currentIssue.open(point);
        saveIssue(currentIssue);
        currentIssue = new Issue();
    }

    @Override
    public Map<BetTypeEnum, Integer> getTopBetMap() {
        return currentIssue.getTopBetMap();
    }

    @Override
    public Map<BetTypeEnum, Integer> getBetMap() {
        return currentIssue.getBetMap();
    }

    @Transactional(rollbackFor = Exception.class)
    protected void saveIssue(Issue currentIssue) {
        // 如果保存错误，在异常处理中将所有投注记录设置为失效（INIT，OPENED, FAIL）
        saveIssueResult(currentIssue.getPoint());
        savePlayerBets(currentIssue.getPlayerMap().values());
        saveBanker(currentIssue.getBankerMap().values());
    }

    private void saveBanker(Collection<Banker> bankers) {
        bankers.forEach(banker -> {
            BankerRecordDO bankerRecordDO = new BankerRecordDO();
            bankerRecordDO.setBankerType(banker.getType());
            bankerRecordDO.setId(banker.getUserId());
            bankerRecordDO.setIssueNo(currentIssue.getIssueNo());
            bankerRecordDO.setAmount(banker.getResult());
        });
    }

    private void savePlayerBets(Collection<Player> players) {

    }

    private void saveIssueResult(Integer point) {
        UpdateWrapper<IssueDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(IssueDO::getIssueNo, currentIssue.getIssueNo())
                .set(IssueDO::getPoint, point);
        issueMapper.update(null, updateWrapper);
    }

    /**
     * 先从当前期中查找，不存在的话，从数据库加载
     * @param id 玩家id
     * @return 玩家对象
     */
    private Player getPlayer(Long id) {
        Player player = currentIssue.getPlayerMap().get(id);
        if (player == null) {
            UserVO userVO = userService.getById(id);
            player = new Player();
            player.setId(userVO.getId());
            player.setMoney(userVO.getMoney());
        }

        return player;
    }
}
