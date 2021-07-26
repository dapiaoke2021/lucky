package com.jxx.gate.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.cola.dto.Response;
import com.jxx.auth.dto.Account;
import com.jxx.auth.service.IAccountService;
import com.jxx.auth.utils.JwtUtil;
import com.jxx.gate.config.IgnoreUrlsConfig;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

/**
 * @author a1
 */
@Component
public class AuthGlobalFilter implements WebFilter {
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Autowired
    IAccountService accountService;

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
        Account account = jwtUtil.parseToken(token);
        ServerHttpRequest request = serverWebExchange.getRequest().mutate()
                .header("id", account.getId().toString())
                .header("role", account.getRole().getName())
                .build();
        serverWebExchange = serverWebExchange.mutate().request(request).build();
        if (isWhite(url)) {
            return webFilterChain.filter(serverWebExchange);
        } else {
            if(accountService.checkAuthority(account.getRole().getName(), url)) {
                return webFilterChain.filter(serverWebExchange);
            } else {
                return forbiddenResponse(serverWebExchange.getResponse(), true);
            }
        }
    }

    private Mono<Void> forbiddenResponse(ServerHttpResponse response, boolean relogin) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body= JSONUtil.toJsonStr(Response.buildFailure("401", relogin ? "请重新登录" : "请登录"));
        DataBuffer buffer =  response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    private boolean isWhite(String url) {
        PathMatcher pathMatcher = new AntPathMatcher();
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, url)) {
                return true;
            }
        }
        return false;
    }
}
