package com.jxx.lucky.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class DuiziGameTest {

    DuiziGame duiziGame;

    @BeforeEach
    public void init() {
        duiziGame = new DuiziGame();
    }

    @Test
    public void testGetGameResult() {
        List<BetResult> betResultList = duiziGame.getBetResults(new String[]{"123.00", "123.00", "123.00", "123.11","123.11" });

        Assertions.assertEquals(
                Arrays.asList(
                        new BetResult(false, 20, BetTypeEnum.DUI_1_5),
                        new BetResult(false, 10, BetTypeEnum.DUI),
                        new BetResult(true, 2, BetTypeEnum.DUI_6_9),
                        new BetResult(true, 2, BetTypeEnum.DUI00),
                        new BetResult(false, 98, BetTypeEnum.DUI11),
                        new BetResult(true, 2, BetTypeEnum.DUI22),
                        new BetResult(true, 2, BetTypeEnum.DUI33),
                        new BetResult(true, 2, BetTypeEnum.DUI44),
                        new BetResult(true, 2, BetTypeEnum.DUI55),
                        new BetResult(true, 2, BetTypeEnum.DUI66),
                        new BetResult(true, 2, BetTypeEnum.DUI77),
                        new BetResult(true, 2, BetTypeEnum.DUI88),
                        new BetResult(true, 2, BetTypeEnum.DUI99)
                ),
                betResultList
        );

        betResultList = duiziGame.getBetResults(new String[]{"123.00", "123.00", "123.00", "123.11","123.12" });
        Assertions.assertEquals(
                Arrays.asList(
                        new BetResult(true, 2, BetTypeEnum.DUI_1_5),
                        new BetResult(true, 2, BetTypeEnum.DUI),
                        new BetResult(true, 2, BetTypeEnum.DUI_6_9),
                        new BetResult(true, 2, BetTypeEnum.DUI00),
                        new BetResult(true, 2, BetTypeEnum.DUI11),
                        new BetResult(true, 2, BetTypeEnum.DUI22),
                        new BetResult(true, 2, BetTypeEnum.DUI33),
                        new BetResult(true, 2, BetTypeEnum.DUI44),
                        new BetResult(true, 2, BetTypeEnum.DUI55),
                        new BetResult(true, 2, BetTypeEnum.DUI66),
                        new BetResult(true, 2, BetTypeEnum.DUI77),
                        new BetResult(true, 2, BetTypeEnum.DUI88),
                        new BetResult(true, 2, BetTypeEnum.DUI99)
                ),
                betResultList
        );
    }
}
