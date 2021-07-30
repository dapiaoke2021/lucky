package com.jxx.auth.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthCheckResultVO implements Serializable {
    AccountVO accountVO;
    boolean authority;
}
