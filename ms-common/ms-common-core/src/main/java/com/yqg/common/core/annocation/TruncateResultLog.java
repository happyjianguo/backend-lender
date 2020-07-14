package com.yqg.common.core.annocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Truncate result to 200 characters
 * Created by ahalim on 2020/07/13
 */
@Inherited
@Retention(RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface TruncateResultLog {

    String value() default "";
}
