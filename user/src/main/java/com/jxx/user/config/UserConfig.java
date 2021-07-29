package com.jxx.user.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * @author a1
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "user")
public class UserConfig {
    BigDecimal platformRebateRate;
}
