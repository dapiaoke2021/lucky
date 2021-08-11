package io.renren.config.pay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信支付配置类
 */
@Component
@ConfigurationProperties(prefix="wechat")
@Data
public class WechatConfig {

    private String  opsAppid;
    private String  opsMchId;
    private String  opsSecretKey;
    private String  opsNotifyUrl;
}
