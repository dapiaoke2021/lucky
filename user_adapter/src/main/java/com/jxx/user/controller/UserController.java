package com.jxx.user.controller;

import com.alibaba.cola.dto.SingleResponse;
import com.jxx.common.aop.UserId;
import com.jxx.user.service.IUserService;
import com.jxx.user.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    IUserService userService;

    @UserId
    @GetMapping
    public SingleResponse<UserVO> getUserInfo(Long userId) {
        log.debug("userId={}", userId);
        return SingleResponse.of(userService.getUser(userId));
    }
}
