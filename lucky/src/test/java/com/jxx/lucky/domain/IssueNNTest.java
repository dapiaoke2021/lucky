package com.jxx.lucky.domain;

import com.jxx.lucky.domain.nn.IssueNN;
import com.jxx.lucky.domain.point.PointGameBanker;
import com.jxx.lucky.param.BetParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@DisplayName("比牛游戏测试")
@ExtendWith(SpringExtension.class)
public class IssueNNTest {

    @InjectMocks
    IssueNN issueNN;


    @Test
    @DisplayName("牛牛坐庄测试")
    public void testBecomeNNBanker() {
        Player player = new Player();
        player.setMoney(9000);
        player.setId(1L);
        assertBecomeBanker(
                player, BankerTypeEnum.NN,
                4500, 4500, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test
    @DisplayName("扫牛坐庄测试")
    public void testBecomeSNBanker() {
        Player player = new Player();
        player.setMoney(9000);
        player.setId(1L);
        assertBecomeBanker(
                player, BankerTypeEnum.SN,
                null, null, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000);
    }

    @Test
    @DisplayName("牛牛下注，最高下注额变更测试")
    public void testBetNNTopBetChange() {
        Player player = new Player();
        player.setMoney(9000);
        player.setId(1L);
        issueNN.becomeBanker(BankerTypeEnum.NN, player);

        Player player3 = new Player();
        player3.setMoney(500);
        issueNN.bet(player3, Collections.singletonList(new BetParam(BetTypeEnum.BET_1, 250)), "betNo");
        assertTopMap(issueNN.getTopBetMap(),
                4250, 4250, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test
    @DisplayName("扫牛下注，最高下注额变更测试")
    public void testBetSNTopBetChange() {
        Player player = new Player();
        player.setMoney(9000);
        player.setId(1L);
        issueNN.becomeBanker(BankerTypeEnum.SN, player);

        Player player3 = new Player();
        player3.setMoney(500);
        issueNN.bet(player3, Collections.singletonList(new BetParam(BetTypeEnum.NUMBER_0, 250)), "betNo");
        assertTopMap(issueNN.getTopBetMap(),
                null, null, 750, 1027, 1027, 1027, 1027, 1027, 1027, 1027, 1027, 1027, 1027, 1027);
    }

    @Test
    @DisplayName("扫牛结算测试")
    public void testSNBetResult() {
        Player player = new Player();
        player.setMoney(9000);
        player.setId(1L);
        issueNN.becomeBanker(BankerTypeEnum.SN, player);

        Player player3 = new Player();
        player3.setMoney(500);
        issueNN.bet(
                player3,
                Arrays.asList(
                        new BetParam(BetTypeEnum.BET_1, 250),
                        new BetParam(BetTypeEnum.BET_2, 250)
                )
                ,
                "betNo");

        //todo: nextdo
        issueNN.open(new String[]{""});
    }

    private void assertBecomeBanker(Player player, BankerTypeEnum bankerType, Integer...expectedTops) {
        issueNN.becomeBanker(bankerType, player);
        Banker banker = issueNN.getBankerMap().get(bankerType);
        assertBanker(player, banker);
        Map<BetTypeEnum, Integer> topBetMap = issueNN.getTopBetMap();
        assertTopMap(topBetMap,expectedTops);
    }

    private void assertTopMap(Map<BetTypeEnum, Integer> topBetMap, Integer...expectedTops) {
        Assertions.assertEquals(expectedTops[0], topBetMap.get(BetTypeEnum.BET_1));
        Assertions.assertEquals(expectedTops[1], topBetMap.get(BetTypeEnum.BET_2));
        Assertions.assertEquals(expectedTops[2], topBetMap.get(BetTypeEnum.NUMBER_0));
        Assertions.assertEquals(expectedTops[3], topBetMap.get(BetTypeEnum.NUMBER_1));
        Assertions.assertEquals(expectedTops[4], topBetMap.get(BetTypeEnum.NUMBER_2));
        Assertions.assertEquals(expectedTops[5], topBetMap.get(BetTypeEnum.NUMBER_3));
        Assertions.assertEquals(expectedTops[6], topBetMap.get(BetTypeEnum.NUMBER_4));
        Assertions.assertEquals(expectedTops[7], topBetMap.get(BetTypeEnum.NUMBER_5));
        Assertions.assertEquals(expectedTops[8], topBetMap.get(BetTypeEnum.NUMBER_6));
        Assertions.assertEquals(expectedTops[9], topBetMap.get(BetTypeEnum.NUMBER_7));
        Assertions.assertEquals(expectedTops[10], topBetMap.get(BetTypeEnum.NUMBER_8));
        Assertions.assertEquals(expectedTops[11], topBetMap.get(BetTypeEnum.NUMBER_9));
    }

    private void assertBanker(Player player, Banker banker) {
        Assertions.assertEquals(banker.getUserId(), player.id);
        Assertions.assertEquals(banker.getMoney(), player.getMoney());
    }
}
