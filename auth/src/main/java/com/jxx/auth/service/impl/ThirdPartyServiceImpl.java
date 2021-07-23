package com.jxx.auth.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.cola.exception.ExceptionFactory;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxx.auth.dos.AccountDO;
import com.jxx.auth.dos.ThirdPartyDO;
import com.jxx.auth.dto.SignPart;
import com.jxx.auth.dto.ThirdParty;
import com.jxx.auth.mapper.AccountMapper;
import com.jxx.auth.mapper.ThirdPartyMapper;
import com.jxx.auth.service.IThirdPartyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author a1
 */
@Service
public class ThirdPartyServiceImpl extends ServiceImpl<ThirdPartyMapper, ThirdPartyDO> implements IThirdPartyService {
    @Override
    public ThirdParty newThirdParty() {
        ThirdPartyDO thirdPartyDO = new ThirdPartyDO();
        thirdPartyDO.setToken(UUID.fastUUID().toString(true));
        if (!save(thirdPartyDO)) {
            throw ExceptionFactory.sysException("保存数据库失败");
        }

        ThirdParty thirdParty = new ThirdParty();
        BeanUtils.copyProperties(thirdPartyDO, thirdParty);
        return thirdParty;
    }

    @Override
    public Boolean authorityByMd5(Long merchantId, String sign, List<SignPart> signParts) {
        ThirdPartyDO thirdPartyDO = getById(merchantId);
        if (thirdPartyDO == null) {
            throw ExceptionFactory.bizException("商户号不存在");
        }
        String signStr = signParts.stream()
                .sorted(Comparator.comparing(SignPart::getName))
                .map(signPart -> signPart.getName() + "=" + signPart.getValue())
                .collect(Collectors.joining("&"));
        return sign.equals(
                DigestUtil.md5Hex(signStr + thirdPartyDO.getToken()).toUpperCase(Locale.ROOT)
        );
    }
}
