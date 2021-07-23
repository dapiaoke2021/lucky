package com.jxx.auth.dto;

import lombok.Data;

/**
 * @author a1
 */
@Data
public class Account {
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String device;
    private Role role;
}
