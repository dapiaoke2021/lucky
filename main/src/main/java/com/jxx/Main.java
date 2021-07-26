package com.jxx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author a1
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.jxx.gate", "com.jxx.common", "com.jxx.auth"})
@MapperScan("com.jxx.auth.mapper")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
