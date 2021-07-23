package com.jxx.auth.param;

import com.jxx.auth.dto.AuthorityTypeEnum;
import lombok.Data;

@Data
public class AuthorityByTokenParam {
    AuthorityTypeEnum type;
    String token;
    String resource;
}
