package com.jxx.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author a1
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.jxx.common", "com.jxx.user"})
@MapperScan("com.jxx.user.mapper")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
