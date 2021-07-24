package com.jxx.gate.domain;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author a1
 */
@Data
public class GatewayFilterDefinition {
    private String name;
    private Map<String, String> args = new LinkedHashMap<>();
}
