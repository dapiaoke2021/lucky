package com.jxx.lucky.domain;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author a1
 */

public enum BetStateEnum {
    // 进行中
    ONGOING(0),
    // 已结算
    SETTLED(1),
    // 已撤销
    REVOKE(2);
    BetStateEnum(int state) {
        this.state = state;
    }

    @EnumValue
    int state;
}
