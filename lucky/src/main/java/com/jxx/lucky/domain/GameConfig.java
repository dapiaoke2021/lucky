package com.jxx.lucky.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GameConfig {
    BankerTypeEnum bankType;
    String gameClass;
}
