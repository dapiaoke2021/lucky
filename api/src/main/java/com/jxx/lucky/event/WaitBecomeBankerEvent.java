package com.jxx.lucky.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WaitBecomeBankerEvent {
    private String bankerType;
    private Integer topBet;
    private Long playerId;
}
