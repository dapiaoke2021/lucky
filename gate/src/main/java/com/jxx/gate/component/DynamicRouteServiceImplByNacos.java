package com.jxx.gate.component;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.jxx.gate.service.impl.DynamicRouteServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author a1
 */
@Component
@Slf4j
public class DynamicRouteServiceImplByNacos {
    private final DynamicRouteServiceImpl dynamicRouteService;

    ConfigService configService;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.cloud.nacos.config.server-addr}")
    private String configServerAddr;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    Yaml yaml;

    @Autowired
    public DynamicRouteServiceImplByNacos(DynamicRouteServiceImpl dynamicRouteService) {
        this.dynamicRouteService = dynamicRouteService;
        yaml = new Yaml();

    }

    public void dynamicRouteByNacosListener (String dataId, String group){
        try {
            configService= NacosFactory.createConfigService(configServerAddr);

            configService.addListener(dataId, group, new Listener()  {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    processConfigInfo(configInfo);
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

    /**
     * 处理配置信息
     *
     * @param configInfo 配置string
     */
    private void processConfigInfo(String configInfo) {
        if (StringUtils.isEmpty(configInfo)) {
            return;
        }
        // 解析yaml文件获取路由定义
        List<RouteDefinition> targetRouteDefinitions = getRouteDefinitionsByYaml(configInfo);
        // 更新路由信息
        dynamicRouteService.refresh(targetRouteDefinitions);
    }

    /**
     * 通过yaml str解析出route定义
     *
     * @param configInfo yaml str
     * @return RouteDefinition array
     */
    private List<RouteDefinition> getRouteDefinitionsByYaml(String configInfo) {
        Yaml yaml = new Yaml();
        Map<Object, Object> document = yaml.load(configInfo);
        List<Map<String, Object>> routeList = (List<Map<String, Object>>) document.get("routes");
        List<RouteDefinition> targetRouteDefinitions = new ArrayList<>(routeList.size());
        for (Map<String, Object> routeItem : routeList) {
            RouteDefinition routeDefinition = new RouteDefinition();
            routeDefinition.setId((String) routeItem.get("id"));
            routeDefinition.setUri(URI.create((String) routeItem.get("uri")));
            List<String> predicateStrList = (List<String>) routeItem.get("predicates");
            List<PredicateDefinition> predicateDefinitions = predicateStrList.stream()
                    .map(PredicateDefinition::new)
                    .collect(Collectors.toList());
            routeDefinition.setPredicates(predicateDefinitions);
            List<String> filterStrList = (List<String>) routeItem.get("filters");
            if (!CollectionUtil.isEmpty(filterStrList)) {
                List<FilterDefinition> filterDefinitions = filterStrList.stream()
                        .map(FilterDefinition::new)
                        .collect(Collectors.toList());
                routeDefinition.setFilters(filterDefinitions);
            }
            Object orderObj = routeItem.get("order");
            int order = Objects.isNull(orderObj) ? 0 : (int) orderObj;
            routeDefinition.setOrder(order);
            targetRouteDefinitions.add(routeDefinition);
        }

        return targetRouteDefinitions;
    }


    @SneakyThrows
    @PostConstruct
    void initRoute() {
        log.info("appName = {}, activeProfile={}", appName, activeProfile);
        dynamicRouteByNacosListener(appName + "-" + activeProfile,"DEFAULT_GROUP");
    }
}
