package com.jxx.gate.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.cola.dto.Response;
import com.jxx.auth.service.IAuthApi;
import com.jxx.auth.vo.AccountVO;
import com.jxx.auth.vo.AuthCheckResultVO;
import com.jxx.gate.config.IgnoreUrls;
import com.jxx.gate.config.IgnoreUrlsConfig;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author a1
 */
//@Component
public class AuthGlobalFilter implements WebFilter {

    @Reference
    IAuthApi authApi;

    @Autowired
    private IgnoreUrls ignoreUrlsConfig;


    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        String token = serverWebExchange.getRequest().getHeaders().getFirst("Authorization");
        String url = serverWebExchange.getRequest().getPath().toString();
        if (StrUtil.isEmpty(token)) {
            if (isWhite(url)) {
                return webFilterChain.filter(serverWebExchange);
            }else{
                return forbiddenResponse(serverWebExchange.getResponse(), false);
            }
        }

        AuthCheckResultVO authCheckResultVO = authApi.checkToken(token, url);
        AccountVO accountVO = authCheckResultVO.getAccountVO();
        if (accountVO == null) {
            return forbiddenResponse(serverWebExchange.getResponse(), true);
        }
        if (!authCheckResultVO.isAuthority()) {
            return forbiddenResponse(serverWebExchange.getResponse(), null);
        }
        ServerHttpRequest request = serverWebExchange.getRequest().mutate()
                .header("id", accountVO.getId().toString())
                .header("role", accountVO.getRoleName())
                .build();
        serverWebExchange = serverWebExchange.mutate().request(request).build();
        return webFilterChain.filter(serverWebExchange);
    }

    private Mono<Void> forbiddenResponse(ServerHttpResponse response, Boolean relogin) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body;
        if (relogin != null) {
            body= JSONUtil.toJsonStr(Response.buildFailure("401", relogin ? "请重新登录" : "请登录"));
        } else {
            body = JSONUtil.toJsonStr(Response.buildFailure("403", "没有访问权限"));
        }

        DataBuffer buffer =  response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    private boolean isWhite(String url) {
        PathMatcher pathMatcher = new AntPathMatcher();
        List<String> ignoreUrls = Arrays.asList(ignoreUrlsConfig.getUrls());
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, url)) {
                return true;
            }
        }
        return false;
    }
}
