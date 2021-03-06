package com.jxx.lucky.domain;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author a1
 */

public enum IssueStateEnum {
    // 下注阶段
    BETTING(0),
    // 结算阶段
    SETTLE(1),
    // 关闭
    CLOSED(2);
    IssueStateEnum(int state) {
        this.state = state;
    }

    @EnumValue
    int state;
}
