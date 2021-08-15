package com.jxx.lucky;

import com.jxx.lucky.component.LuckyEventSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.jxx.common", "com.jxx.lucky"})
@MapperScan("com.jxx.lucky.mapper")
@EnableBinding(LuckyEventSource.class)
public class LuckyApplication {
    public static void main(String[] args) {
        SpringApplication.run(LuckyApplication.class, args);
    }
}
