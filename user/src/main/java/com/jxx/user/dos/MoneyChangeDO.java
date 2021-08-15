package com.jxx.user.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@TableName("money_change")
@Data
public class MoneyChangeDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Timestamp createTime;
    private Integer type;
    private String description;
    private Integer amount;
    private Long userId;
}
