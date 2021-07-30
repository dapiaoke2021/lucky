package com.jxx.lucky.controller;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.jxx.lucky.domain.BetTypeEnum;
import com.jxx.lucky.service.IssueService;
import com.jxx.lucky.vo.BetVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("lucky")
@RestController
public class LuckyController {

    @Autowired
    IssueService issueService;

    @GetMapping("/bet")
    public MultiResponse<BetVO> getBet() {
        Map<BetTypeEnum, Integer> betMap = issueService.getBetMap();
        Map<BetTypeEnum, Integer> topMap = issueService.getTopBetMap();
        List<BetVO> bets = new ArrayList<>();
        for (BetTypeEnum betType : betMap.keySet()) {
            BetVO betVO = new BetVO();
            betVO.setBetType(betType);
            betVO.setBet(betMap.get(betType));
            betVO.setTop(topMap.get(betType));
            bets.add(betVO);
        }
        return MultiResponse.of(bets);
    }

    @PostMapping("/{betType}")
    public Response bet(Long playerId, @PathVariable BetTypeEnum betType, Integer amount) {
        issueService.bet(playerId, amount, betType);
        return Response.buildSuccess();
    }


}
