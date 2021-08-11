package io.renren.common.annotation;

import java.lang.annotation.*;

/**
 * 将解密之后的数据注入
 * 使用了此注解的参数
 * @author liuyuchan286
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RsaParams {

	String value() default "";
}
