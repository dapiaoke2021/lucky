package io.renren.common.base.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 枚举类输出前端的实体类
 * @author liuyuchan286
 * @email liuyuchan286@gmail.com
 * @date 2019/6/18
 */
@Data
@Accessors(chain = true)
public class EnumVo {
    private Integer key;
    private String value;
    private List<EnumVo> children;
}
