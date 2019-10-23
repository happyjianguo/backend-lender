package com.yqg.common.core.annocation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 请求参数必须大于零的整数
 * Created by gaohaiming on 2018/6/18.
 */
@Inherited
@Retention(RUNTIME)
@Target({ FIELD })
public @interface ReqIntGreaterThan0 {

    String value() default "";
}
