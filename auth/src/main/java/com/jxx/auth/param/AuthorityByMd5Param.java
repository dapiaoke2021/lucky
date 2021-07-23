package com.jxx.auth.param;

import lombok.Data;

@Data
public class AuthorityByMd5Param {
    Long merchantId;
    String sign;
}
