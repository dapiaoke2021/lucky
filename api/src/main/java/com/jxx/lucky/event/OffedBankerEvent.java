package com.jxx.lucky.event;

import lombok.Data;

@Data
public class OffedBankerEvent {
    private Long playerId;
    private Integer result;
}
