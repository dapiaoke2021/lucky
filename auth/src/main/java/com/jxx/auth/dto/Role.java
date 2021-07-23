package com.jxx.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author a1
 */
@AllArgsConstructor
@Data
public class Role {
    private String name;
    private List<String> urls;

    public Role(String name) {
        this.name = name;
    }
}
