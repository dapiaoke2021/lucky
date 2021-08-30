package com.jxx.lucky.vo;

import com.jxx.lucky.domain.nn.NiuEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2021/8/29 14:19
 */
@Data
public class CurrentIssueDataVO {

    private BigDecimal hign;
    private BigDecimal low;
    private BigDecimal close;
    private NiuEnum hignPoint;
    private NiuEnum lowPoint;
    private NiuEnum closePoint;
    private String currentIssueNo;

    public BigDecimal getHign() {
        return hign.setScale(2);
    }

    public BigDecimal getLow() {
        return low.setScale(2);
    }

    public BigDecimal getClose() {
        return close.setScale(2);
    }
}
