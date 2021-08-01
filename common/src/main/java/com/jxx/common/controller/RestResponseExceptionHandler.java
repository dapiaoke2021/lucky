package com.jxx.common.controller;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;
import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BizException.class)
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        BizException bizException = (BizException) ex;
        Response response = Response.buildFailure(bizException.getErrCode(), bizException.getMessage());
        String bodyOfResponse = JSON.toJSONString(response);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return handleExceptionInternal(ex, bodyOfResponse,
                httpHeaders, HttpStatus.OK, request);
    }
}
