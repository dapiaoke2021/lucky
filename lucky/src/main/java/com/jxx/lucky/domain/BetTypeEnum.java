package com.jxx.lucky.domain;

/**
 * 押注类型
 * 顺序不能更改，只能添加。数据库中存储的下注类型与此定义一致。
 * @author a1
 */

public enum BetTypeEnum {
    // 大小单双
    SMALL, BIG, OOD, EVEN,
    // 波胆0-9
    NIU_1, NIU_2, NIU_3, NIU_4, NIU_5, NIU_6, NIU_7, NIU_8, NIU_9, NIU_NIU,
    // 闲1 闲2（牛牛）
    BET_1, BET_2,
    // 对子
    DUI_1_5, DUI, DUI_6_9,
    DUI11, DUI22, DUI33, DUI44, DUI55, DUI66, DUI77, DUI88, DUI99, DUI00,
    NIL
}
