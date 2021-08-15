package com.jxx.lucky.config;

import com.jxx.lucky.domain.BankerTypeEnum;
import com.jxx.lucky.domain.GameConfig;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Data
public class IssueGameProperty {
    List<GameConfig> gameConfig;
}
