package com.yqg.common.exceptions;

/**
 * 异常枚举接口
 * Created by gao on 2017/12/19.
 */
public interface IExceptionEnum {

    /**
     * 获取错误code
     * @return
     */
    int getCode();

    /**
     * 获取错误信息
     * @return
     */
    String getMessage();

    /**
     * 获取错误信息,国际化内容
     * @return
     */
    String getMessageI18n();

}
