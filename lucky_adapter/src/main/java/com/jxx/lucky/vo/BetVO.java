package com.jxx.lucky.vo;

import com.jxx.lucky.domain.BankerTypeEnum;
import com.jxx.lucky.domain.Bet;
import com.jxx.lucky.domain.BetTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@ApiModel("注单")
@Data
public class BetVO {
    @ApiModelProperty("期号")
    private String issueNo;

    @ApiModelProperty("注单号")
    private String betNo;

    @ApiModelProperty("下注额")
    private Map<BetTypeEnum, Integer> bets;

    @ApiModelProperty("盈亏")
    private Map<BetTypeEnum, Integer> results;

    @ApiModelProperty("庄家类型")
    private BankerTypeEnum bankerType;

    @ApiModelProperty("剩余usdt")
    private Integer money;

    @Data
    @AllArgsConstructor
    static public class BetItemVO{
        BetTypeEnum betType;
        private Integer bet;
        private Integer result;
    }
    List<BetItemVO> betItemVOS;
}
