package com.yqg.common.core.annocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 不需要进行登录校验
 * Created by gaohaiming on 2018/6/18.
 */
@Inherited
@Retention(RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface NotNeedLogin {

    String value() default "";
}
