package com.jxx.gate.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.jxx.gate.service.impl.DynamicRouteServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;

/**
 * @author a1
 */
@Component
@Slf4j
public class DynamicRouteServiceImplByNacos {
    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    ConfigService configService;

    public DynamicRouteServiceImplByNacos() {
        dynamicRouteByNacosListener("sc-gateway","DEFAULT_GROUP");
    }

    public void dynamicRouteByNacosListener (String dataId, String group){
        try {
            configService= NacosFactory.createConfigService("127.0.0.1:8849");

            configService.addListener(dataId, group, new Listener()  {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    RouteDefinition definition= JSON.parseObject(configInfo,RouteDefinition.class);
                    dynamicRouteService.update(definition);
                }
                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (NacosException e) {
            log.error("nacos error:{}", e);
        }
    }

    @SneakyThrows
    @PostConstruct
    void initRoute() {
        String content = configService.getConfig("sc-gateway","DEFAULT_GROUP", 5000);
        RouteDefinition definition= JSON.parseObject(content,RouteDefinition.class);
        dynamicRouteService.update(definition);
    }
}
