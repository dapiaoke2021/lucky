package com.jxx.gate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "secure.ignore")
public class IgnoreUrls {
    private String[] urls;
}
