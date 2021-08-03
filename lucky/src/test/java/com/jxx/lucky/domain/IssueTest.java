package com.jxx.lucky.domain;

import com.alibaba.cola.exception.BizException;
import com.jxx.lucky.param.BetParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@ExtendWith(SpringExtension.class)
public class IssueTest {
    Issue issue;

    @BeforeEach
    public void init() {
        issue = new Issue();
    }

    @Test
    public void becomeBanker() {
        Player player = new Player();
        player.setMoney(10000);
        player.setId(1L);
        assertBecomeBanker(player, BankerTypeEnum.BIG_SMALL, 10000, 10000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        Map<BetTypeEnum, Integer> topBetMap = issue.getTopBetMap();
        // 有人坐庄的情况
        Throwable throwable = Assertions.assertThrows(BizException.class, () -> {
            issue.becomeBanker(BankerTypeEnum.BIG_SMALL, player);
        });
        Assertions.assertEquals(throwable.getMessage(), "已有玩家坐庄");
        assertTopMap(topBetMap, 10000, 10000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        // 已是庄家坐庄
        Throwable throwable1 = Assertions.assertThrows(BizException.class, () -> {
           issue.becomeBanker(BankerTypeEnum.OOD_EVEN, player);
        });
        Assertions.assertEquals(throwable1.getMessage(), "已经是庄家");
        assertTopMap(topBetMap, 10000, 10000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        // 单双坐庄
        Player player1 = new Player();
        player1.setMoney(20000);
        player1.setId(2L);
        assertBecomeBanker(player1, BankerTypeEnum.OOD_EVEN,
                10000, 10000, 20000, 20000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        Player player2 = new Player();
        player2.setMoney(100000);
        player2.setId(3L);
        assertBecomeBanker(player2, BankerTypeEnum.NUMBER,10000, 10000,
                20000, 20000,
                11111, 11111, 11111, 11111, 11111, 11111, 11111, 11111, 11111, 11111 );

        Player player3 = new Player();
        player3.setMoney(500);
        Throwable throwable2 = Assertions.assertThrows(BizException.class, () -> {
            issue.bet(player3, Collections.singletonList(new BetParam(BetTypeEnum.SMALL, 501)));
        });
        Assertions.assertEquals(throwable2.getMessage(), "金额不足");

        Throwable throwable3 = Assertions.assertThrows(BizException.class, () -> {
            issue.bet(player2, Collections.singletonList(new BetParam(BetTypeEnum.SMALL, 20000)));
        });
        Assertions.assertEquals(throwable3.getMessage(), "大小庄家额度不足");

        issue.bet(
                player3,
                Arrays.asList(
                        new BetParam(BetTypeEnum.SMALL, 100),
                        new BetParam(BetTypeEnum.NUMBER_0, 100),
                        new BetParam(BetTypeEnum.NUMBER_1, 100),
                        new BetParam(BetTypeEnum.EVEN, 100))
        );
        assertTopMap(issue.getTopBetMap(),10100, 9900,
                19900, 20100,
                11022, 11022, 11133, 11133, 11133, 11133, 11133, 11133, 11133, 11133  );

        Player player4 = new Player();
        player4.setId(4L);
        player4.setMoney(1000);
        issue.bet(player4, Collections.singletonList(new BetParam(BetTypeEnum.BIG, 100)));
        assertTopMap(issue.getTopBetMap(),10000, 10000,
                19900, 20100,
                11022, 11022, 11133, 11133, 11133, 11133, 11133, 11133, 11133, 11133  );

        issue.open(0);
        Assertions.assertEquals(player3.getBonus(), 1345);
        Assertions.assertEquals(player3.getMoney(), 100);
        Assertions.assertEquals(player4.getBonus(), 0);
        Assertions.assertEquals(player4.getMoney(), 900);

        Assertions.assertEquals(issue.getBankerMap().get(BankerTypeEnum.OOD_EVEN).getResult(), -100);
        Assertions.assertEquals(issue.getBankerMap().get(BankerTypeEnum.BIG_SMALL).getResult(), -5);
        Assertions.assertEquals(issue.getBankerMap().get(BankerTypeEnum.NUMBER).getResult(), -805);
    }

    private void assertBecomeBanker(Player player, BankerTypeEnum bankerType, Integer...expectedTops) {
        issue.becomeBanker(bankerType, player);
        Banker banker = issue.getBankerMap().get(bankerType);
        assertBanker(player, banker);
        Map<BetTypeEnum, Integer> topBetMap = issue.getTopBetMap();
        assertTopMap(topBetMap,expectedTops);
    }

    private void assertTopMap(Map<BetTypeEnum, Integer> topBetMap, Integer...expectedTops) {
        Assertions.assertEquals(expectedTops[0], topBetMap.get(BetTypeEnum.BIG));
        Assertions.assertEquals(expectedTops[1], topBetMap.get(BetTypeEnum.SMALL));
        Assertions.assertEquals(expectedTops[2], topBetMap.get(BetTypeEnum.EVEN));
        Assertions.assertEquals(expectedTops[3], topBetMap.get(BetTypeEnum.OOD));
        Assertions.assertEquals(expectedTops[4], topBetMap.get(BetTypeEnum.NUMBER_0));
        Assertions.assertEquals(expectedTops[5], topBetMap.get(BetTypeEnum.NUMBER_1));
        Assertions.assertEquals(expectedTops[6], topBetMap.get(BetTypeEnum.NUMBER_2));
        Assertions.assertEquals(expectedTops[7], topBetMap.get(BetTypeEnum.NUMBER_3));
        Assertions.assertEquals(expectedTops[8], topBetMap.get(BetTypeEnum.NUMBER_4));
        Assertions.assertEquals(expectedTops[9], topBetMap.get(BetTypeEnum.NUMBER_5));
        Assertions.assertEquals(expectedTops[10], topBetMap.get(BetTypeEnum.NUMBER_6));
        Assertions.assertEquals(expectedTops[11], topBetMap.get(BetTypeEnum.NUMBER_7));
        Assertions.assertEquals(expectedTops[12], topBetMap.get(BetTypeEnum.NUMBER_8));
        Assertions.assertEquals(expectedTops[13], topBetMap.get(BetTypeEnum.NUMBER_9));
    }

    private void assertBanker(Player player, Banker banker) {
        Assertions.assertEquals(banker.getUserId(), player.id);
        Assertions.assertEquals(banker.getTopBet(), player.getMoney());
    }
}
