package com.jxx.lucky.domain;

import com.alibaba.cola.exception.BizException;
import com.jxx.lucky.param.BetParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
public class NNGameTest {

    NNGame nnGame;

    @BeforeEach
    public void init() {
        nnGame = new NNGame();
    }

    @Test
    @DisplayName("上庄最高点数测试")
    public void testBankerTopBet() {
        Player player = new Player();
        player.setMoney(9000);
        player.setId(1L);
        nnGame.becomeBanker(player);

        assertTopBet(nnGame.topBetMap, 1800, 1800);
    }

    @DisplayName("押注最高点数测试_超过最高下注")
    @Test
    public void testBetTopBet1() {
        Player banker = new Player();
        banker.setMoney(1999);
        banker.setId(1L);
        nnGame.becomeBanker(banker);

        Player player = new Player();
        player.setMoney(2000);
        player.setId(2L);
        Throwable throwable1 = Assertions.assertThrows(BizException.class, () -> {
            nnGame.checkBet(player, Collections.singletonList(new BetParam(BetTypeEnum.BET_1, 400, null)));
            nnGame.bet(
                    player,
                    Collections.singletonList(new BetParam(BetTypeEnum.BET_1, 400, null)),
                    "betNo1");
        });
        Assertions.assertEquals("牛牛庄家额度不足", throwable1.getMessage());
    }

    @DisplayName("押注测试——超过玩家最高额度")
    @Test
    public void testPlayerTopBet() {
        Player banker = new Player();
        banker.setMoney(9000);
        banker.setId(1L);
        nnGame.becomeBanker(banker);

        Player player = new Player();
        player.setMoney(2000);
        player.setId(2L);
        Throwable throwable1 = Assertions.assertThrows(BizException.class, () -> {
            nnGame.checkBet(player, Collections.singletonList(new BetParam(BetTypeEnum.BET_1, 401, null)));
            nnGame.bet(
                    player,
                    Collections.singletonList(new BetParam(BetTypeEnum.BET_1, 401, null)),
                    "betNo1");
        });
        Assertions.assertEquals("金额不足", throwable1.getMessage());
    }

    @DisplayName("押注最高点数测试_未超过最高下注")
    @Test
    public void testBetTopBet2() {
        Player banker = new Player();
        banker.setMoney(9000);
        banker.setId(1L);
        nnGame.becomeBanker(banker);

        Player player = new Player();
        player.setMoney(3000);
        player.setId(2L);
        nnGame.bet(
                player,
                Arrays.asList(
                        new BetParam(BetTypeEnum.BET_1, 100, null),
                        new BetParam(BetTypeEnum.BET_2, 200, null)
                ),
                "betNo1");
        assertTopBet(nnGame.topBetMap, 1500, 1500);
    }

    @DisplayName("开奖测试")
    @Test
    public void openTest() {
        Player banker = new Player();
        banker.setMoney(9000);
        banker.setId(1L);
        nnGame.becomeBanker(banker);

        Player player2 = new Player();
        player2.setMoney(3000);
        player2.setId(2L);
        nnGame.bet(
                player2,
                Arrays.asList(
                        new BetParam(BetTypeEnum.BET_1, 100, null),
                        new BetParam(BetTypeEnum.BET_2, 200, null)
                ),
                "betNo1");
        Player player3 = new Player();
        player3.setMoney(3000);
        player3.setId(3L);
        nnGame.bet(
                player3,
                Arrays.asList(
                        new BetParam(BetTypeEnum.BET_1, 300, null),
                        new BetParam(BetTypeEnum.BET_2, 300, null)
                ),
                "betNo2");

        nnGame.open(new String[]{"12304.06", "12304.06", "12364.06", "12304.06", "12304.06"});

        Assertions.assertEquals(480, player2.bonus);
        Assertions.assertEquals(1440, player3.bonus);
        Assertions.assertEquals(-1125, nnGame.banker.result);
        Assertions.assertEquals(9000-1125, nnGame.banker.money);
    }

    private void assertTopBet(Map<BetTypeEnum, Integer> topBet, Integer ...bets) {
        Map<BetTypeEnum, Integer> topBetExpect = new HashMap<BetTypeEnum, Integer>(){{
            put(BetTypeEnum.BET_1, bets[0]);
            put(BetTypeEnum.BET_2, bets[1]);
        }};
        Assertions.assertEquals(topBetExpect, topBet);
    }
}
