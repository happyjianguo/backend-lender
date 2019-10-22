package com.yqg.common.enums;

import com.yqg.common.exceptions.IExceptionEnum;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

/**
 * Created by liyixing on 2018/9/3.
 */
public enum OrderExceptionEnums implements IExceptionEnum {
    SEND_CREDITORINFO_ERROR(1001,"标的已存在", ""),
    SELECT_SCATTERSTANDARD_ERROR(1002,"标的不存在", ""),
    USER_NOT_EXIST(1003,"用户不存在", ""),
    ARGS_EXCEPTION(1004,"参数异常", ""),
    ORDER_NOT_EXIST(1005,"订单不存在", ""),
    LOCKAMOUT_ERR(1006,"锁定金额异常", ""),
    SIGE_ERROR(1007,"推标验签失败", ""),
    PRODUCT_ERROR(1008,"产品信息不存在", ""),
    CART_NOT_FOUND_GOODS_ERROR(1009, "购物车内对应商品不存在","Item yang sesuai dalam keranjang belanja tidak ada.")//购物车内对应商品不存在
    ;


    private int code;
    private String message;
    private String messageI18n;
    private String temlate;

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getMessageI18n() {
        return this.messageI18n;
    }

    private OrderExceptionEnums(int code, String message, String messageI18n) {
        this.code = code;
        this.message = message;
        this.messageI18n = messageI18n;
    }

    public OrderExceptionEnums setCustomMessage(String message) {
        this.message = message;
        return this;
    }

    public OrderExceptionEnums setCustomMessage(String message, String messageI18n) {
        this.message = message;
        this.messageI18n = messageI18n;
        return this;
    }

    private OrderExceptionEnums(int code, String message, String messageI18n, String template) {
        this.code = code;
        this.message = message;
        this.messageI18n = messageI18n;
        this.temlate = template;
    }

    public OrderExceptionEnums setDynamicMessage(String... ags) {
        String dynamicMsg = MessageFormat.format(this.temlate, ags);
        String dynamicMsgI18n = MessageFormat.format(this.temlate, ags);
        if(!StringUtils.isEmpty(dynamicMsg)) {
            this.message = dynamicMsg;
        }

        if(!StringUtils.isEmpty(dynamicMsgI18n)) {
            this.messageI18n = dynamicMsgI18n;
        }

        return this;
    }
}
