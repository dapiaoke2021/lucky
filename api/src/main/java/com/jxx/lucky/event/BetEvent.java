package com.jxx.lucky.event;

import lombok.Data;

@Data
public class BetEvent {
    private Long playerId;
    private Integer betAmount;
}
