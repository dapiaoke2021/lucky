package com.jxx.user.dos;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MoneyChangeDO {
    private Timestamp createTime;
    private Integer type;
    private String description;
    private Integer amount;
}
