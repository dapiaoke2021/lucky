package com.jxx.auth.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author a1
 */
@TableName("third_party")
@Data
public class ThirdPartyDO {
    private Long id;
    private String token;
}
