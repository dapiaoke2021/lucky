package com.jxx.lucky.config;

import com.jxx.lucky.service.IPointGenerator;
import com.jxx.lucky.service.impl.BtcUsdtPointGenerator;
import com.jxx.lucky.service.impl.EthUsdtPointGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PointGeneratorConfig {

    @Bean
    @ConditionalOnProperty(name = "point-source", havingValue = "eth")
    public IPointGenerator createEthPointGenerator() {
        return new EthUsdtPointGenerator();
    }

    @Bean
    @ConditionalOnProperty(name = "point-source", havingValue = "btc")
    public IPointGenerator createBtcPointGenertor() {
        return new BtcUsdtPointGenerator();
    }
}
