package com.jxx.lucky.domain.nn;

import com.jxx.lucky.domain.Banker;
import com.jxx.lucky.domain.BetTypeEnum;
import com.jxx.lucky.domain.BetTypeResult;

import java.util.List;
import java.util.Map;

public class SNBanker extends Banker {
    @Override
    public int open(Map<BetTypeEnum, Integer> betMap, List<BetTypeResult> bankerBetTypeResults) {
        return 0;
    }
}
