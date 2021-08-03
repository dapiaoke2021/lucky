package com.jxx.lucky.event;

import lombok.Data;

@Data
public class BecameBankerEvent {
    private String bankerType;
    private Integer topBet;
    private Long playerId;
}
