package com.jxx.lucky.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BetEvent {
    private Long playerId;
    private Integer betAmount;
}
