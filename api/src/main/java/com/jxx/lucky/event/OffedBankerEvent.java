package com.jxx.lucky.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OffedBankerEvent {
    private Long playerId;
    private Integer money;
}
