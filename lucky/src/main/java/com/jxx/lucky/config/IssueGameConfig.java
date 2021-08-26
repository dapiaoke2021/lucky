package com.jxx.lucky.config;

import com.jxx.lucky.domain.BankerTypeEnum;
import com.jxx.lucky.domain.GameConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class IssueGameConfig {
    @Bean
    @ConditionalOnProperty(name = "point-source", havingValue = "btc")
    public IssueGameProperty newBtcGameConfig() {
        IssueGameProperty btcGameConfig = new IssueGameProperty();
        btcGameConfig.setGameConfig(Arrays.asList(
                new GameConfig(BankerTypeEnum.NN, "com.jxx.lucky.domain.NNGame"),
                new GameConfig(BankerTypeEnum.SN, "com.jxx.lucky.domain.PointGame")
        ));
        return btcGameConfig;
    }

    @Bean
    @ConditionalOnProperty(name = "point-source", havingValue = "eth")
    public IssueGameProperty newEthGameConfig() {
        IssueGameProperty ethGameConfig = new IssueGameProperty();
        ethGameConfig.setGameConfig(Arrays.asList(
                new GameConfig(BankerTypeEnum.NN, "com.jxx.lucky.domain.NNGame2"),
                new GameConfig(BankerTypeEnum.DUI, "com.jxx.lucky.domain.DuiziGame")
        ));
        return ethGameConfig;
    }
}
