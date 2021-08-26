package com.jxx.auth.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountVO  implements Serializable {
    private Long id;
    private String roleName;
    private String username;
    private String phone;
}
