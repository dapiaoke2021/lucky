package io.renren.modules.app.utils;

import java.math.BigDecimal;

/**
 * @author liuyuchan286
 * @email liuyuchan286@gmail.com
 * @date 2019/6/28
 */
public class CompareUtil {


    /**
     * onw > two i > 0
     * onw = two i = 0
     * onw < two i < 0
     * 如果 one 大于 two 返回true
     * 否则返回false
     * @param one
     * @param two
     * @return
     */
    public static boolean compareToBigDecimal(BigDecimal one,BigDecimal two) {
        int i = one.compareTo(two);
        return i > 0 ? true : false;
    }
}
