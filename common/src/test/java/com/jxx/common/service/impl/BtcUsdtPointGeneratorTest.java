package com.jxx.common.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@ExtendWith(SpringExtension.class)
public class BtcUsdtPointGeneratorTest {

    @Test
    public void testGetPoint() {
        BtcUsdtPointGenerator btcUsdtPointGenerator = new BtcUsdtPointGenerator();
        Integer point = btcUsdtPointGenerator.getPoint("08032128");
        Assertions.assertEquals(4, point);
    }
}
