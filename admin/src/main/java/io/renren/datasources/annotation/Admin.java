package io.renren.datasources.annotation;

import com.baomidou.dynamic.datasource.annotation.DS;
import io.renren.datasources.constant.DBConstants;

import java.lang.annotation.*;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2021-08-11 16:49
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DS(DBConstants.DATASOURCE_ADMIN)
public @interface Admin {
}
