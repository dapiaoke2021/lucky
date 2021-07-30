package com.jxx.lucky;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.jxx.lucky.mapper")
public class LuckyApplication {
    public static void main(String[] args) {
        SpringApplication.run(LuckyApplication.class, args);
    }
}
