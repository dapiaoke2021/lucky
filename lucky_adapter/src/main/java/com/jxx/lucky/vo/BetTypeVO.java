package com.jxx.lucky.vo;

import com.jxx.lucky.domain.BetTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("下注区信息")
@Data
public class BetTypeVO {
    @ApiModelProperty("押注类型")
    private BetTypeEnum betType;

    @ApiModelProperty("总押注额")
    private Integer bet;

    @ApiModelProperty("最高押注额")
    private Integer top;
}
