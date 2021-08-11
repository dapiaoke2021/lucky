//package io.renren.modules.app.utils;
//
//import cn.hutool.crypto.SecureUtil;
//import cn.hutool.json.JSONUtil;
//import io.renren.modules.ops.dto.UserTokenDto;
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//
///**
// * @author liuyuchan286
// * @email liuyuchan286@gmail.com
// * @date 2020-09-24 14:52
// */
//@Component
//@ConfigurationProperties(prefix = "encrypt")
//@Data
//public class EncryptUtil {
//
//    private String opsKey;
//
//    public String generateToken(String openid,String phone,String ip) {
//        //生成token
//        UserTokenDto userTokenDto = new UserTokenDto();
//        userTokenDto.setOpenid(openid);
//        userTokenDto.setPhone(phone);
//        userTokenDto.setIp(ip);
//        userTokenDto.setTimestamp(System.currentTimeMillis());
//        String token = SecureUtil.aes(opsKey.getBytes()).encryptBase64(JSONUtil.toJsonStr(userTokenDto));
//        return token;
//    }
//
//}
