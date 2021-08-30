package com.jxx.lucky.config;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BinanceClientConfig {


    @Bean
    public SyncRequestClient syncRequestClient(){
        RequestOptions options = new RequestOptions();
        options.setUrl("https://api.binance.com");
        SyncRequestClient syncRequestClient = SyncRequestClient.create("P9ZTWSIFxu7s5KjJ2aZovMywRhuRaOsHWdaZjTpZx25yD8wgjcGLCguov7qdWc8Q", "ZSu4ehNCBDiapRefL4hFj454YwysMIbHvoGssIXnt8ly9zPhBm91sOpbWeWtNrU6",
                options);
        return syncRequestClient;
    }
}
