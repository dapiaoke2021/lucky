package com.jxx.user.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author a1
 */
@Data
public class UserVO implements Serializable {
    private Long id;
    private Integer money;
    private Long upper;
}
