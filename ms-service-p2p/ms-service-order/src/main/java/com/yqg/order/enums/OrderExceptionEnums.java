package com.yqg.order.enums;

import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.IExceptionEnum;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

/**
 * Created by liyixing on 2018/9/3.
 */
public enum OrderExceptionEnums implements IExceptionEnum {
    SEND_CREDITORINFO_ERROR(1001,"标的已存在"),
    SELECT_SCATTERSTANDARD_ERROR(1002,"标的不存在"),
    USER_NOT_EXIST(1003,"用户不存在"),
    ARGS_EXCEPTION(1004,"参数异常"),
    ORDER_NOT_EXIST(1005,"订单不存在"),
    LOCKAMOUT_ERR(1006,"锁定金额异常"),
    SIGE_ERROR(1007,"推标验签失败"),
    PRODUCT_ERROR(1006,"产品信息不存在");


    private int code;
    private String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    private OrderExceptionEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 可选2 根据占位符填充动态信息
     */
    private String temlate;
    private OrderExceptionEnums(int code, String message, String template) {
        this.code = code;
        this.message = message;
        this.temlate=template;
    }

    /**
     * 根据占位符填充动态信息
     *
     * @param ags
     * @return
     */
    public OrderExceptionEnums setDynamicMessage(String... ags) {
        String dynamicMsg = MessageFormat.format(this.temlate, ags);
        if (!StringUtils.isEmpty(dynamicMsg)) {
            this.message = dynamicMsg;
        }
        return this;
    }
}
