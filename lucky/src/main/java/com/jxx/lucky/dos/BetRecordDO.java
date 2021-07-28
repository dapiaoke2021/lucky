package com.jxx.lucky.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jxx.lucky.domain.BetStateEnum;
import com.jxx.lucky.domain.BetTypeEnum;
import lombok.Data;

/**
 * @author a1
 */
@TableName("bet_record")
@Data
public class BetRecordDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long playerId;
    private BetTypeEnum betType;
    private Integer money;
    private String issueNo;
    private BetStateEnum state;
}
