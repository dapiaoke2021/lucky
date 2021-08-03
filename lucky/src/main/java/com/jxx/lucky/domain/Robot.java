package com.jxx.lucky.domain;

import lombok.Data;

import java.util.Objects;

@Data
public class Robot extends Player{
    /**
     * 准备下庄后继续的期数
     */
    private Integer onGoingIssueCount;

    /**
     * 庄家类型
     */
    private BankerTypeEnum bankerType;

}
