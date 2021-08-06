package com.jxx.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@Component
@Aspect
public class UserIdInjector {

    @Pointcut("execution(* com.jxx.*.controller.*.*(Integer ,..))")
    public void filterControllerWithInteger() {

    }

    @Pointcut("@annotation(com.jxx.common.aop.UserId)")
    public void filterControllerWithUserId() {

    }

    @Around("filterControllerWithUserId()")
    public Object injectUserId(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.debug("userId inject request={}", request);
        Long userId = Long.valueOf(request.getHeader("id"));
        Object[] args = pjp.getArgs();
        args[0] = userId;
        return pjp.proceed(args);
    }
}
