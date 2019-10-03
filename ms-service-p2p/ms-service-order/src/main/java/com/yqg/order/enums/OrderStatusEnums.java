package com.yqg.order.enums;

/**
 * Created by huanhuan on 2018/9/5.
 */
public enum OrderStatusEnums  {
    //1.投资处理中 2.投资失败 3,投资成功  4.回款处理中 5.回款失败 6.回款成功 7.失效订单
    //1.投资处理中 2.投资失败 3,投资成功 4匹配成功 5回款清分中 6.回款处理中 7.回款失败 8.回款成功 9.失效订单
    INVESTMENTING(1,"投资处理中"),
    INVESTMEN_FAIL(2,"投资失败"),
    INVESTMEN_SUCCESS(3,"投资成功"),
    MATCH_SUCCESS(4,"匹配成功"),
    REPAYING(5,"回款清分中"),
    REPAY(6,"回款处理中"),
    REPAY_FAIL(7,"回款失败"),
    REPAY_SUCCESS(8,"回款成功"),
    FAILORDER(9,"失效订单")
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
