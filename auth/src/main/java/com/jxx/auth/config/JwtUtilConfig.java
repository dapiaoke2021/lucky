package com.jxx.auth.config;

import com.jxx.auth.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtUtilConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    JwtUtil createJwtUtil() {
        return new JwtUtil(secret);
    }
}
