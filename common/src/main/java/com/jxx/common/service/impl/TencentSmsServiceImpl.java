package com.jxx.common.service.impl;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.jxx.common.service.ISmsService;

/**
 * @author a1
 */
@Slf4j
@Service
public class TencentSmsServiceImpl implements ISmsService {

    @Value("${sms.tencent.apiId}")
    private String apiId;
    @Value("${sms.tencent.apiKey}")
    private String apiKey;
    @Value("${sms.tencent.appId}")
    private String appId;
    @Value("${sms.tencent.sign}")
    private String sign;


    @Override
    public void sendSms(String phone, String templateId, String[] templateParams) {
        try {
            Credential cred = new Credential(apiId, apiKey);
            SmsClient client = new SmsClient(cred, "ap-guangzhou");
            SendSmsRequest req = new SendSmsRequest();
            req.setSmsSdkAppid(appId);
            req.setSign(sign);
            String senderId = "";
            req.setSenderId(senderId);
            req.setTemplateID(templateId);
            String[] phoneNumbers = {"+86" + phone};
            req.setPhoneNumberSet(phoneNumbers);
            req.setTemplateParamSet(templateParams);
            client.SendSms(req);
        } catch (Exception e) {
            log.error("secretId:{}\nsecretKye:{}\nSDKAppId:{}\nsign:{}\ntemplateId：{}",
                    apiId, apiKey, appId, sign, templateId);
            e.printStackTrace();
        }
    }
}
