package com.jxx.lucky.controller;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxx.common.aop.UserId;
import com.jxx.lucky.domain.BankerTypeEnum;
import com.jxx.lucky.domain.BetTypeEnum;
import com.jxx.lucky.dos.BetRecordDO;
import com.jxx.lucky.mapper.BetMapper;
import com.jxx.lucky.mapper.IssueMapper;
import com.jxx.lucky.service.IssueService;
import com.jxx.lucky.vo.BetTypeVO;
import com.jxx.lucky.param.BetParam;
import com.jxx.lucky.vo.BetVO;
import com.jxx.lucky.vo.IssueBankerVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/lucky")
@RestController
public class LuckyController {

    @Autowired
    IssueService issueService;

    @Autowired
    BetMapper betMapper;

    @ApiOperation("下注区域信息")
    @GetMapping("/betType")
    public MultiResponse<BetTypeVO> getBetType() {
        Map<BetTypeEnum, Integer> betMap = issueService.getBetMap();
        Map<BetTypeEnum, Integer> topMap = issueService.getTopBetMap();
        List<BetTypeVO> bets = new ArrayList<>();
        for (BetTypeEnum betType : topMap.keySet()) {
            BetTypeVO betVO = new BetTypeVO();
            betVO.setBetType(betType);
            betVO.setBet(betMap.get(betType));
            betVO.setTop(topMap.get(betType));
            bets.add(betVO);
        }
        return MultiResponse.of(bets);
    }

    @UserId
    @ApiOperation("下注")
    @PostMapping
    public SingleResponse<String> bet(Long playerId, @RequestBody List<BetParam> bets) {
        String betNo = issueService.bet(playerId, bets);
        return SingleResponse.of(betNo);
    }

    @UserId
    @ApiOperation("上庄")
    @PostMapping("/banker")
    public Response becomeBanker(Long playerId, BankerTypeEnum bankerType, Integer money) {
        issueService.becomeBanker(playerId, bankerType, money);
        return Response.buildSuccess();
    }

    @UserId
    @ApiOperation("下庄")
    @DeleteMapping("/banker")
    public Response offBanker(Long playerId, BankerTypeEnum bankerType) {
        issueService.offBanker(playerId, bankerType);
        return Response.buildSuccess();
    }

    @ApiOperation("获取庄家信息")
    @GetMapping("/banker")
    public SingleResponse<IssueBankerVO> listBanker() {
        IssueBankerVO issueBankerVO = new IssueBankerVO();
        issueBankerVO.setCurrentBanker(issueService.getCurrentBanker());
        issueBankerVO.setBankerQueueMap(issueService.getWaitBanker());
        return SingleResponse.of(issueBankerVO);
    }

    @UserId
    @ApiOperation("获取下注记录")
    @GetMapping("/bet")
    public PageResponse<BetVO> listBetRecord(Long playerId, Page<BetRecordDO> page) {
        QueryWrapper<BetRecordDO> listQueryWrapper = new QueryWrapper<>();
        listQueryWrapper.select("distinct bet_no", "create_time")
                .lambda()
                .eq(BetRecordDO::getPlayerId, playerId)
                .orderByDesc(BetRecordDO::getCreateTime);
        Page<BetRecordDO> betRecordPage = betMapper.selectPage(page, listQueryWrapper);
        if (betRecordPage.getRecords().isEmpty()) {
            return PageResponse.buildSuccess();
        }

        Map<String, BetVO> betVOMap = new HashMap<>();
        QueryWrapper<BetRecordDO> listQueryWrapper1 = new QueryWrapper<>();
        listQueryWrapper1.lambda()
                        .in(
                                BetRecordDO::getBetNo,
                                betRecordPage.getRecords().stream()
                                        .map(BetRecordDO::getBetNo).collect(Collectors.toList())
                        );
        List<BetRecordDO> betRecordDOS =  betMapper.selectList(listQueryWrapper1);
        betRecordDOS.forEach(betRecordDO -> {
            BetVO betVO = betVOMap.get(betRecordDO.getBetNo());
            if (betVO == null) {
                betVO = new BetVO();
                betVOMap.put(betRecordDO.getBetNo(), betVO);
            }
            log.debug("betVO={} betRecordDO={}", betVO, betRecordDO);
            betVO.setBetNo(betRecordDO.getBetNo());
            betVO.setIssueNo(betRecordDO.getIssueNo());
            if (betVO.getBets() == null) {
                betVO.setBets(new HashMap<>());
            }
            if (betVO.getResults() == null) {
                betVO.setResults(new HashMap<>());
            }
            betVO.getBets().merge(BetTypeEnum.values()[betRecordDO.getBetType()], betRecordDO.getMoney(), Integer::sum);
            betVO.getResults().merge(BetTypeEnum.values()[betRecordDO.getBetType()], betRecordDO.getResult(), Integer::sum);
        });

        betVOMap.forEach((issueNo, betVO) -> {
            betVO.setBetItemVOS(
                    betVO.getBets().keySet().stream().map(betTypeEnum -> {
                        Map<BetTypeEnum, Integer> bets = betVO.getBets();
                        Map<BetTypeEnum, Integer> results = betVO.getResults();
                        return new BetVO.BetItemVO(betTypeEnum, bets.get(betTypeEnum), results.get(betTypeEnum));
                    }).collect(Collectors.toList())
            );
        });

        return PageResponse.of(
                betVOMap.values(),
                (int)betRecordPage.getTotal(),
                (int)betRecordPage.getSize(),
                (int)betRecordPage.getCurrent());
    }

}
