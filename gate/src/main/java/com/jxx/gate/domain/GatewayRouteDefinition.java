package com.jxx.gate.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author a1
 */
@Data
public class GatewayRouteDefinition {
    private String id;
    private List<GatewayPredicateDefinition> predicates = new ArrayList<>();
    private List<GatewayFilterDefinition> filters = new ArrayList<>();
    private String uri;
    private int order = 0;

    public GatewayRouteDefinition() {
    }
}
