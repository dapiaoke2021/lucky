package io.renren.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.hutool.core.io.IoUtil;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import io.renren.config.pay.WechatConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信mp配置
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-07-70 19:22
 */
@Configuration
public class WxMpConfig {

    @Autowired
    private WechatConfig wechatConfig;

    @Bean
    public WxMaDefaultConfigImpl wxMaConfigOps() {
        WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
        wxMaConfig.setAppid(wechatConfig.getOpsAppid());
        wxMaConfig.setSecret(wechatConfig.getOpsSecretKey());
        return wxMaConfig;
    }

//    @Bean
//    public WxMaDefaultConfigImpl wxMaConfigDelivery() {
//        WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
//        wxMaConfig.setAppid(wechatConfig.getDeliveryAppid());
//        wxMaConfig.setSecret(wechatConfig.getDeliverySecretKey());
//        return wxMaConfig;
//    }

//    @Bean
//    public WxPayConfig wxPayConfig() {
//        WxPayConfig wxPayConfig = new WxPayConfig();
//        wxPayConfig.setAppId(wechatConfig.getMemberAppid());
//        wxPayConfig.setMchId(wechatConfig.getMemberMchId());
//        wxPayConfig.setMchKey(wechatConfig.getMemberMchKey());
//        wxPayConfig.setNotifyUrl(wechatConfig.getMemberNotifyUrl());
//        //设置Cert.p12
//        wxPayConfig.setKeyContent(IoUtil.readBytes(this.getClass().getClassLoader().getResourceAsStream(wechatConfig.getMemberCert())));
//        return wxPayConfig;
//    }


    @Bean(name = "wxMaServiceOps")
    public WxMaService wxMaServiceOps() {
        WxMaService wxMpService = new WxMaServiceImpl();
        wxMpService.setWxMaConfig(wxMaConfigOps());
        return wxMpService;
    }

//    @Bean(name = "wxMaServiceDelivery")
//    public WxMaService wxMaServiceDelivery() {
//        WxMaService wxMpService = new WxMaServiceImpl();
//        wxMpService.setWxMaConfig(wxMaConfigDelivery());
//        return wxMpService;
//    }
//
//    @Bean
//    public WxPayService wxPayService() {
//        WxPayService wxPayService = new WxPayServiceImpl();
//        wxPayService.setConfig(wxPayConfig());
//        return wxPayService;
//    }






}
