package com.jxx.common.service;

/**
 * @author a1
 */
public interface ISmsService {
    /**
     * 发送短信
     * @param phone 电话号码
     * @param templateId 短信模板
     * @param templateParams 内容
     */
    void sendSms(String phone, String templateId, String[] templateParams);
}
