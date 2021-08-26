package com.jxx.gate.config;

import com.jxx.auth.service.IAuthApi;
import com.jxx.auth.service.impl.IAuthApiImpl;
import com.jxx.auth.utils.JwtUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "role")
public class AuthConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${dummy}")
    private Integer dummy;

    @Setter
    @Getter
    Map<String, List<String>> authority;

    @Bean
    public IAuthApi createAuthApi() {
        IAuthApiImpl iAuthApi = new IAuthApiImpl(new JwtUtil(secret));
        iAuthApi.setAuthority(authority);
        return iAuthApi;
    }
}
