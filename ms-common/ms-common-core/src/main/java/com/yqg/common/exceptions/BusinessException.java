package com.yqg.common.exceptions;

/**
 * 业务异常类,用于业务过程中主动抛出给客户端的异常,会被格式化成json
 * Created by gao on 2017/12/19.
 */
public class BusinessException extends Exception{

    private IExceptionEnum exceptionEnum;

    public BusinessException(IExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    public IExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    public void setExceptionEnum(IExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }
}
