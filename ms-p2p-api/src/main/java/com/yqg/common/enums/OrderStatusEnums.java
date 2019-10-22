package com.yqg.common.enums;

/**
 * Created by huanhuan on 2018/9/5.
 */
public enum OrderStatusEnums  {

    INVESTMENTING(1,"投资处理中"),
    INVESTMEN_FAIL(2,"投资失败"),
    INVESTMEN_SUCCESS(3,"投资成功"),

    PAYING(8,"支付处理中"),
    FAILORDER(9,"订单失效")
    ;


    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private OrderStatusEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
