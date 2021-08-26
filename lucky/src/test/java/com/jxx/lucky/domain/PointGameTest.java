package com.jxx.lucky.domain;

import com.alibaba.cola.exception.BizException;
import com.jxx.lucky.param.BetParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@DisplayName("比点游戏测试")
public class PointGameTest {

    PointGame pointGame;

    @BeforeEach
    public void init() {
        pointGame = new PointGame();
    }

    @Test
    @DisplayName("上庄最高点数测试")
    public void testBankerTopBet() {
        Player player = new Player();
        player.setMoney(9000);
        player.setId(1L);
        pointGame.becomeBanker(player);

        assertTopBet(pointGame.topBetMap, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000);
    }

    @DisplayName("押注最高点数测试_超过最高下注")
    @Test
    public void testBetTopBet1() {
        Player banker = new Player();
        banker.setMoney(9000);
        banker.setId(1L);
        pointGame.becomeBanker(banker);

        Player player = new Player();
        player.setMoney(2000);
        player.setId(2L);
        Throwable throwable1 = Assertions.assertThrows(BizException.class, () -> {
            pointGame.checkBet(
                    player,
                    Collections.singletonList(new BetParam(BetTypeEnum.NIU_1, 1001, null)));
            pointGame.bet(
                    player,
                    Collections.singletonList(new BetParam(BetTypeEnum.NIU_1, 1001, null)),
                    "betNo1");
        });
        Assertions.assertEquals("扫牛庄家额度不足", throwable1.getMessage());
    }

    @DisplayName("押注最高点数测试_未超过最高下注")
    @Test
    public void testBetTopBet2() {
        Player banker = new Player();
        banker.setMoney(9000);
        banker.setId(1L);
        pointGame.becomeBanker(banker);

        Player player = new Player();
        player.setMoney(3000);
        player.setId(2L);
        pointGame.bet(
                player,
                Arrays.asList(
                        new BetParam(BetTypeEnum.NIU_1, 900, null),
                        new BetParam(BetTypeEnum.NIU_2, 900, null)
                ),
                "betNo1");
        assertTopBet(pointGame.topBetMap, 200, 200, 1200, 1200, 1200, 1200, 1200, 1200, 1200, 1200);
    }

    @DisplayName("开奖测试")
    @Test
    public void openTest() {
        Player banker = new Player();
        banker.setMoney(9000);
        banker.setId(1L);
        pointGame.becomeBanker(banker);

        Player player2 = new Player();
        player2.setMoney(3000);
        player2.setId(2L);
        pointGame.bet(
                player2,
                Arrays.asList(
                        new BetParam(BetTypeEnum.NIU_1, 900, null),
                        new BetParam(BetTypeEnum.NIU_2, 900, null)
                ),
                "betNo1");

        Player player3 = new Player();
        player3.setMoney(3000);
        player3.setId(3L);
        pointGame.bet(
                player3,
                Arrays.asList(
                        new BetParam(BetTypeEnum.NIU_3, 1000, null),
                        new BetParam(BetTypeEnum.NIU_4, 900, null)
                ),
                "betNo2");

        pointGame.open(new String[]{"12304.06", "12304.06", "12304.06", "12304.06", "12304.06"});

        Assertions.assertEquals(0, player2.bonus);
        Assertions.assertEquals(9550, player3.bonus);
        Assertions.assertEquals(-6435, pointGame.banker.result);
        Assertions.assertEquals(2565, pointGame.banker.money);
    }

    private void assertTopBet(Map<BetTypeEnum, Integer> topBet, Integer ...bets) {
        Map<BetTypeEnum, Integer> topBetExpect = new HashMap<BetTypeEnum, Integer>(){{
            put(BetTypeEnum.NIU_1, bets[0]);
            put(BetTypeEnum.NIU_2, bets[1]);
            put(BetTypeEnum.NIU_3, bets[2]);
            put(BetTypeEnum.NIU_4, bets[3]);
            put(BetTypeEnum.NIU_5, bets[4]);
            put(BetTypeEnum.NIU_6, bets[5]);
            put(BetTypeEnum.NIU_7, bets[6]);
            put(BetTypeEnum.NIU_8, bets[7]);
            put(BetTypeEnum.NIU_9, bets[8]);
            put(BetTypeEnum.NIU_NIU, bets[9]);
        }};
        Assertions.assertEquals(topBetExpect, topBet);
    }
}
