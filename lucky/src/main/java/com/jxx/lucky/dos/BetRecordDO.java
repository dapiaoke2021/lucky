package com.jxx.lucky.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jxx.lucky.domain.BetResultEnum;
import com.jxx.lucky.domain.BetStateEnum;
import com.jxx.lucky.domain.BetTypeEnum;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author a1
 */
@TableName("bet_record")
@Data
public class BetRecordDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long playerId;
    private Integer betType;
    private Integer money;
    private String issueNo;
    private BetStateEnum state;
    private Integer bet;
    private Integer bankerType;
    private String pointSource;

    private Timestamp createTime;

    private Integer result;

    /**
     * 下注单号 playerId-YYYYMMDDHHmmSS
     */
    private String betNo;
}
