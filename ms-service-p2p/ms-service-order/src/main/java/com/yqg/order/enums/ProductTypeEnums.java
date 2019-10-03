package com.yqg.order.enums;

/**
 * Created by huanhuan on 2018/9/5.
 */
public enum ProductTypeEnums {
    //产品类型--1.散标 2活期 3.理财
    SCATTER_STANDARD(1,"散标"),
    CURRENT_ACCOUNT_DEPOSIT (2,"活期"),
    FINANCING(3,"理财")
    ;


    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private ProductTypeEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
