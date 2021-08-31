package com.jxx.lucky.domain;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 庄家类型
 * 顺序不能更改，只能添加。数据库中存储的下注类型与此定义一致。
 * @author a1
 */
public enum BankerTypeEnum {
    // 大小
    BIG_SMALL(0),
    // 单双
    OOD_EVEN(1),
    // 波胆
    NUMBER(2),
    // 扫牛
    SN(3),
    // 牛牛
    NN(4),
    // 对子
    DUI(5);

    BankerTypeEnum(Integer bankerType) {
        this.bankerType = bankerType;
    }

    @EnumValue
    Integer bankerType;
}
