package io.renren.common.utils;

import cn.hutool.core.util.ObjectUtil;
import io.renren.common.base.interfaces.BaseEnum;
import io.renren.common.base.vo.EnumVo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 枚举工具类
 */
public class EnumUtil {

    public static String describe(BaseEnum[] baseEnums) {
        String describe = enumToVo(baseEnums).stream().map(enumVo -> enumVo.getKey() + ":" + enumVo.getValue() + " ,").collect(Collectors.joining());
        return describe;
    }

    public static List<EnumVo> enumToVo(BaseEnum[] baseEnums) {
        return Arrays.stream(baseEnums).map(EnumUtil::toEnumVo).collect(Collectors.toList());
    }


    public static List<EnumVo> enumToVo(List<BaseEnum> baseEnums) {
        return baseEnums.stream().map(EnumUtil::toEnumVo).collect(Collectors.toList());
    }

    public static String queryValueByKey(Integer key,BaseEnum[] baseEnums) {
        BaseEnum baseEnum1 = Arrays.stream(baseEnums).filter(baseEnum -> baseEnum.getKey().equals(key)).findAny().orElse(null);
        return ObjectUtil.isNotNull(baseEnum1) ? baseEnum1.getValue() : null;
    }


    private static EnumVo toEnumVo(BaseEnum baseEnum) {
        return new EnumVo().setKey(baseEnum.getKey()).setValue(baseEnum.getValue());
    }

}
