package com.yqg.common.utils;

import java.util.UUID;

/**
 * uuid工具
 * Created by gao on 2018/6/21.
 */
public class UuidUtil {
    /**
     * 生成uuid
     *
     * @return
     */
    public static String create() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
