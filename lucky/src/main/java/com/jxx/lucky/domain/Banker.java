package com.jxx.lucky.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public abstract class Banker {
    protected Long userId;
    protected Integer money;
    protected Integer result;
    protected BankerTypeEnum type;

    abstract public int open(Map<BetTypeEnum, Integer> betMap, List<BetTypeResult> bankerBetTypeResults);
}
