package com.yqg.common.core.response;

import com.yqg.common.exceptions.IExceptionEnum;
import lombok.Data;

/**
 * 基础返回值格式
 * Created by gao on 2017/12/19.
 */
@Data
public class BaseResponse<T> {
    /**
     * 返回值状态码,非0即错误
     */
    private int code;
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 返回值信息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;


    public static final int SUCCESS_CODE = 0;
    public static final String SUCCESS_MESSAGE = "success";

    /**
     * 自定义信息和结果的成功返回值
     *
     * @param message
     * @param data
     * @return
     */
    public BaseResponse<T> successResponse(String message, T data) {
        this.setCode(SUCCESS_CODE);
        this.setSuccess(true);
        this.setMessage(message);
        this.setData(data);
        return this;
    }

    /**
     * 自定义结果的成功返回值
     *
     * @param data
     * @return
     */
    public BaseResponse<T> successResponse(T data) {

        return this.successResponse(SUCCESS_MESSAGE, data);
    }

    /**
     * 错误
     * @param exceptionEnum
     * @param openI18n
     * @return
     */
    public BaseResponse<T> errorResponse(IExceptionEnum exceptionEnum, boolean openI18n){
        this.setCode(exceptionEnum.getCode());
        this.setSuccess(false);
        if(openI18n){
            this.setMessage(exceptionEnum.getMessageI18n());
        }else {
            this.setMessage(exceptionEnum.getMessage());
        }
        return this;
    }

    /**
     * 默认成功返回值
     *
     * @return
     */
    public BaseResponse<T> successResponse() {
        return this.successResponse(SUCCESS_MESSAGE, null);
    }
}
