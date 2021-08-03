package com.jxx.lucky.event;

import lombok.Data;

@Data
public class WaitBecomeBankerEvent {
    private String bankerType;
    private Integer topBet;
    private Long playerId;
}
