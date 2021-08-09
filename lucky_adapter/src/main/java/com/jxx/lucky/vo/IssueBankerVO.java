package com.jxx.lucky.vo;

import com.jxx.lucky.domain.point.PointGameBanker;
import com.jxx.lucky.domain.BankerTypeEnum;
import com.jxx.lucky.domain.Player;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApiModel("庄家信息")
@Data
public class IssueBankerVO {
    @ApiModelProperty("当前庄家")
    private Map<BankerTypeEnum, PointGameBanker> currentBanker;

    @ApiModelProperty("队列中庄家")
    private Map<BankerTypeEnum, ConcurrentLinkedQueue<Player>> bankerQueueMap;
}
