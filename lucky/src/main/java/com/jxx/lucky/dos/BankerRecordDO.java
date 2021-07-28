package com.jxx.lucky.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jxx.lucky.domain.BankerTypeEnum;
import lombok.Data;

/**
 * @author a1
 */
@TableName("banker_record")
@Data
public class BankerRecordDO {
    private Long id;
    private String issueNo;
    private Long playerId;
    private BankerTypeEnum bankerType;
    private Integer amount;
}
