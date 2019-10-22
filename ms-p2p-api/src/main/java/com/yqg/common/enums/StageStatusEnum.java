package com.yqg.common.enums;

/**
 * @Author: hyy
 * @Date: 2019/7/4 15:35
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
public enum  StageStatusEnum {
    REFUND_WATING(1,"待还款"),
    REFUNDING(2,"还款处理中"),
    RESOLVED(3,"已还款"),
    OVERDUE_RESOLVED(4,"逾期已还款");

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private StageStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
