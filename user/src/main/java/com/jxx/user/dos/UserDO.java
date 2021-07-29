package com.jxx.user.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author a1
 */
@TableName("user")
@Data
public class UserDO {
    private Long id;
    private Integer money;
    private Long upper;
    private BigDecimal rebateRate;
}
