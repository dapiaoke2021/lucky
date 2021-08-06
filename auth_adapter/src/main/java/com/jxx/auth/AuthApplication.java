package com.jxx.auth;

import com.jxx.auth.component.AuthEventSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author a1
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.jxx.common", "com.jxx.auth"})
@MapperScan("com.jxx.auth.mapper")
@EnableBinding(AuthEventSource.class)
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
