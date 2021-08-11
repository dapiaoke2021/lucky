package io.renren.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import io.renren.config.swagger.SwaggerProperties;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig implements WebMvcConfigurer {

    @Autowired
    private FileConfig fileConfig;
    @Autowired
    private HostNameConfig hostNameConfig;

    @Bean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/swagger/**").addResourceLocations("classpath:/static/swagger/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/config/**").addResourceLocations("classpath:/static/config/");
        registry.addResourceHandler("/1902151001/**").addResourceLocations("classpath:/static/1902151001/");
        registry.addResourceHandler("/h5/**").addResourceLocations("classpath:/static/h5/");
        registry.addResourceHandler("/file/**").addResourceLocations("file:///" + fileConfig.getSaveurl());
    }


    @Bean
    public Docket createRestApi() {
        SwaggerProperties swaggerProperties = swaggerProperties();
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            //加了ApiOperation注解的类，才生成接口文档
            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
            //包下的类，才生成接口文档
//            .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(security())
            .host(hostNameConfig.getName());
    }

    private ApiInfo apiInfo() {
        SwaggerProperties swaggerProperties = swaggerProperties();
        return new ApiInfoBuilder()
            .title(swaggerProperties.getTitle())
            .contact(swaggerProperties.getContact())
            .description(swaggerProperties.getDescription())
            .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
            .version(swaggerProperties.getVersion())
            .build();
    }

    private List<ApiKey> security() {
        return newArrayList(
            new ApiKey("token", "token", "header")
        );
    }

}
