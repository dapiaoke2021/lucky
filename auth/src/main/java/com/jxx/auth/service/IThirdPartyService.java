package com.jxx.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jxx.auth.dos.ThirdPartyDO;
import com.jxx.auth.dto.SignPart;
import com.jxx.auth.dto.ThirdParty;

import java.util.List;

/**
 * @author a1
 */
public interface IThirdPartyService extends IService<ThirdPartyDO> {
    /**
     * 新建第三方账户
     * @return
     */
    ThirdParty newThirdParty();

    /**
     * md5验证
     * @param merchantId 商户号
     * @param sign 签名
     * @param signParts 参与字段与值
     * @return 签名结果 true通过 false失败
     */
    Boolean authorityByMd5(Long merchantId, String sign, List<SignPart> signParts);
}
