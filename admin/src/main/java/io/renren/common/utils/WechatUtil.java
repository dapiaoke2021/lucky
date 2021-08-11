package io.renren.common.utils;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import io.renren.common.exception.RRException;
import io.renren.config.pay.WechatConfig;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
@Component
@Slf4j
public class WechatUtil {


    @Resource(name = "wxMaServiceOps")
    private WxMaService wxMaServiceOps;
    @Autowired
    private WechatConfig wechatConfig;
//    @Autowired
//    private WxPayService wxPayService;
//
//    @Resource(name = "wxMaServiceDelivery")
//    private WxMaService wxMaServiceDelivery;


    /**
     * 根据Code获取用户信息
     * @param code
     * @return
     */
    public WxMaJscode2SessionResult getSessionInfo(String code) {
        return getWxMaJscode2SessionResult(wxMaServiceOps, code);
    }


    /**
     * 获取用户授权信息
     * @param sessionKey
     * @param encryptedData
     * @param ivStr
     * @return
     */
    public WxMaUserInfo getUserInfo(String sessionKey, String encryptedData, String ivStr) {
        return getWxMaUserInfo(wxMaServiceOps, sessionKey,encryptedData,ivStr);
    }



//    /**
//     * 根据code获取openid
//     * @param code
//     * @return
//     */
//    public WxMaJscode2SessionResult getSessionInfoDelivery(String code) {
//        return getWxMaJscode2SessionResult(wxMaServiceDelivery, code);
//    }

//    /**
//     * 获取用户手机号
//     * @param sessionKey
//     * @param encryptedData
//     * @param ivStr
//     * @return
//     */
//    public WxMaPhoneNumberInfo getPhoneNoInfo(String sessionKey, String encryptedData, String ivStr) {
//        return wxMaServiceMember.getUserService().getPhoneNoInfo(sessionKey, encryptedData, ivStr);
//    }

//    /**
//     * 获取用户手机号
//     * @param sessionKey
//     * @param encryptedData
//     * @param ivStr
//     * @return
//     */
//    public WxMaPhoneNumberInfo getPhoneNoInfoDelivery(String sessionKey, String encryptedData, String ivStr) {
//        return wxMaServiceDelivery.getUserService().getPhoneNoInfo(sessionKey, encryptedData, ivStr);
//    }


//    @Async
//    public void sendSubscribeMsg(WxMaSubscribeMessage wxMaSubscribeMessage) {
//        try {
//            wxMaServiceMember.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }
//    }


//    /**
//     * 创建订单
//     * @param openid
//     * @param totalFee
//     * @param code
//     * @param method
//     * @return
//     */
//    public WxPayMpOrderResult createOrder(String openid,BigDecimal totalFee, String code, String method) {
//        try {
//            WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
//            wxPayUnifiedOrderRequest.setNonceStr(String.valueOf(RandomUtils.nextInt()));
//            wxPayUnifiedOrderRequest.setBody("支付");
//            wxPayUnifiedOrderRequest.setOutTradeNo(code);
//            wxPayUnifiedOrderRequest.setTotalFee(totalFee.multiply(BigDecimal.valueOf(100)).intValue());
//            wxPayUnifiedOrderRequest.setSpbillCreateIp("127.0.0.1");
//            wxPayUnifiedOrderRequest.setNotifyUrl(String.format(wechatConfig.getMemberNotifyUrl(),method,code));
//            wxPayUnifiedOrderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);
//            wxPayUnifiedOrderRequest.setOpenid(openid);
//            WxPayMpOrderResult order = wxPayService.createOrder(wxPayUnifiedOrderRequest);
//            return order;
//        } catch (WxPayException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    public void refund(String outTradeNo,String outRefundNo,BigDecimal totalFee,BigDecimal refundFee) throws WxPayException {
//        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
//        wxPayRefundRequest.setOutTradeNo(outTradeNo);
//        wxPayRefundRequest.setOutRefundNo(outRefundNo);
//        wxPayRefundRequest.setTotalFee(totalFee.multiply(BigDecimal.valueOf(100)).intValue());
//        wxPayRefundRequest.setRefundFee(refundFee.multiply(BigDecimal.valueOf(100)).intValue());
//        wxPayRefundRequest.setSignType(WxPayConstants.SignType.MD5);
//        wxPayService.refund(wxPayRefundRequest);
//    }

//    /**
//     * 判断订单是否支付成功
//     * @param request
//     * @return
//     */
//    public boolean verify(HttpServletRequest request) {
//        String xml = ServletUtil.getBody(request);
//        try {
//            WxPayOrderNotifyResult wxPayOrderNotifyResult = wxPayService.parseOrderNotifyResult(xml);
//            return StrUtil.isNotBlank(wxPayOrderNotifyResult.getOutTradeNo());
//        } catch (WxPayException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    public String success() {
        String ok = WxPayNotifyResponse.success("OK");
        return ok;
    }

    public String fail() {
        String fail = WxPayNotifyResponse.fail("fail");
        return fail;
    }

    private WxMaJscode2SessionResult getWxMaJscode2SessionResult(WxMaService service,String code) {
        try {
            WxMaJscode2SessionResult wxMaJscode2SessionResult = service.jsCode2SessionInfo(code);
            return wxMaJscode2SessionResult;
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new RRException(e.getMessage(),402);
        }
    }

    private WxMaUserInfo getWxMaUserInfo(WxMaService service,String sessionKey, String encryptedData, String ivStr) {
        WxMaUserInfo userInfo = service.getUserService().getUserInfo(sessionKey, encryptedData, ivStr);
        return userInfo;
    }


}
