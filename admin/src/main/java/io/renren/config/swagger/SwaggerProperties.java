package io.renren.config.swagger;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("swagger")
@Accessors
public class SwaggerProperties {

    private String title;
    private String description;
    private String termsOfServiceUrl;
    private String host;
    private String version;
    private String contact;
    private String basePackage;

}
