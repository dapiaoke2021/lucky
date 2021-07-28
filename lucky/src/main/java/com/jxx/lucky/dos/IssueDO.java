package com.jxx.lucky.dos;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jxx.lucky.domain.IssueStateEnum;
import lombok.Data;

/**
 * @author a1
 */
@TableName("issue")
@Data
public class IssueDO {
    @TableId
    private String issueNo;
    private Integer point;
    private IssueStateEnum state;
    private Long smallBigBanker;
    private Long oodEvenBanker;
    private Long numberBanker;
}
