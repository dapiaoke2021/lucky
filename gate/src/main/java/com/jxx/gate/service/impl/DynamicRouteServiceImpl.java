package com.jxx.gate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author a1
 */
@Slf4j
@Service
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {
    private RouteDefinitionRepository routeDefinitionRepository;

    public DynamicRouteServiceImpl(
            RouteDefinitionRepository routeDefinitionRepository) {
        this.routeDefinitionRepository = routeDefinitionRepository;

    }

    private ApplicationEventPublisher publisher;


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * merge更新路由
     *
     * 保证刷新逻辑不存在线程安全问题，刷新路由并没有很高的性能需求，此处锁住整个refresh方法。
     * @param definitions 路由详情集合
     */
    public synchronized void refresh(List<RouteDefinition> definitions) {
        // 填充生成order
        fillTargetRouteOrder(definitions);
        // 目标routes id集合
        List<String> targetDefIds = definitions.stream().map(RouteDefinition::getId).collect(Collectors.toList());
        // 获取现存所有路由map
        Map<String, RouteDefinition> aliveRouteMap = getAliveRouteMap();
        // 删除失效的routes
        removeDefinitions(targetDefIds, aliveRouteMap);
        // 更新definitions
        updateDefinitions(definitions, aliveRouteMap);
        // 添加definitions
        createDefinitions(definitions, aliveRouteMap);
        // 发布路由已更新时间
        publishRouteChangedEvent();
    }

    private void publishRouteChangedEvent() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    private void createDefinitions(List<RouteDefinition> definitions, Map<String, RouteDefinition> aliveRouteMap) {
        // 获取新添加的definitions
        Set<String> aliveRouteIdSet = aliveRouteMap.keySet();
        List<RouteDefinition> needCreateDefs =
                definitions
                        .stream()
                        .filter(route -> !aliveRouteIdSet.contains(route.getId()))  // 不存在与当前存活集合
                        .collect(Collectors.toList());
        doCreateDefinitions(needCreateDefs);
    }

    private void doCreateDefinitions(List<RouteDefinition> needCreateDefs) {
        needCreateDefs.forEach(createDef -> {
            try {
                this.routeDefinitionRepository.save(Mono.just(createDef)).subscribe();
                log.info("created route: {}", createDef.getId());
            } catch (Exception e) {
                e.printStackTrace();
                log.info("create route {} fail", createDef.getId());
            }
        });
    }

    private void updateDefinitions(List<RouteDefinition> definitions, Map<String, RouteDefinition> aliveRouteMap) {
        Set<String> aliveRouteIdSet = aliveRouteMap.keySet();
        List<RouteDefinition> needUpdateDefs =
                definitions
                        .stream()
                        .filter(route -> aliveRouteIdSet.contains(route.getId())
                                && !route.equals(aliveRouteMap.get(route.getId())))  // 当前存活且与当前definition不同则为更新
                        .collect(Collectors.toList());
        doUpdateDefinitions(needUpdateDefs);
    }

    private void doUpdateDefinitions(List<RouteDefinition> needUpdateDefs) {
        needUpdateDefs.forEach(updateDefinition -> {
            try {
                this.routeDefinitionRepository
                        .delete(Mono.just(updateDefinition.getId()))
                        .subscribe();
                log.info("removed old route(will be recreate): {}", updateDefinition.getId());
            } catch (Exception e) {
                e.printStackTrace();
                log.info("can't clean route(will be create): {}", updateDefinition.getId());
            }
            try {
                this.routeDefinitionRepository.save(Mono.just(updateDefinition)).subscribe();
                log.info("updated route: {}", updateDefinition.getId());
            } catch (Exception e) {
                e.printStackTrace();
                log.info("updated route {} fail", updateDefinition.getId());
            }
        });
    }

    private void removeDefinitions(List<String> targetDefIds, Map<String, RouteDefinition> aliveRouteMap) {
        List<String> removedDefinitionIds =
                aliveRouteMap
                        .keySet()
                        .stream()
                        .filter(routeId -> !targetDefIds.contains(routeId))
                        .collect(Collectors.toList());
        doRemoveDefinitions(removedDefinitionIds);
    }

    private void doRemoveDefinitions(List<String> removedDefinitionIds) {
        removedDefinitionIds.forEach(removedId -> {
            this.routeDefinitionRepository
                    .delete(Mono.just(removedId))
                    .subscribe();
            log.info("removed route: {}", removedId);
        });
    }

    private Map<String, RouteDefinition> getAliveRouteMap() {
        return routeDefinitionRepository
                .getRouteDefinitions()
                .toStream()
                .collect(Collectors.toMap(RouteDefinition::getId, Function.identity()));
    }


    private void fillTargetRouteOrder(List<RouteDefinition> definitions) {
        int order = 1;
        for (RouteDefinition route : definitions) {
            if (route.getOrder() == 0) {
                route.setOrder(order++);
            }
        }
    }
}
