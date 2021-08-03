package com.jxx.lucky.domain;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum BetResultEnum {
    LOSE(0), WIN(1);

    @EnumValue
    private Integer result;
    BetResultEnum(int result) {
        this.result = result;
    }
}
