package com.jxx.auth.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author a1
 */
@TableName("role")
@Data
public class RoleDO {
    private Long id;
    private String name;
    private String resources;
}
