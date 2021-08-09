package com.jxx.lucky.domain.nn;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum NiuEnum {
    NIL(0),
    NIU_1(1), NIU_2(2), NIU_3(3), NIU_4(4), NIU_5(5),
    NIU_6(6), NIU_7(7), NIU_8(8), NIU_9(9),
    NIU_NIU(10);

    NiuEnum(int val) {
        this.val = val;
    }

    @EnumValue
    private int val;
}
