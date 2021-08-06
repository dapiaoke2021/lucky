package com.jxx.user.enums;

import lombok.Getter;

@Getter
public enum MoneyChangeTypeEnum {
    GAME(0, "投注结算"),
    SALE(1, "提成"),
    TRANSFER(3, "转账"),
    BET(4, "下注"),
    BECOME_BANKER(5, "上庄"),
    OFF_BANKER(6, "下庄"),
    ;

    private int type;
    private String description;

    MoneyChangeTypeEnum(int type, String description) {
        this.type = type;
        this.description = description;
    }
}
