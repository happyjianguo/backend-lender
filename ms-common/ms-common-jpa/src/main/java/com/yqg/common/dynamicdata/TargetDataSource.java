package com.yqg.common.dynamicdata;

import java.lang.annotation.*;

/**
 * Created by gao on 2018/6/28.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String dataSource() default "";//数据源
}
