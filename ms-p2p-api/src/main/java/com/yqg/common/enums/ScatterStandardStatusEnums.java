package com.yqg.common.enums;

/**
 * Created by huanhuan on 2018/9/5.
 */
public enum ScatterStandardStatusEnums {
    THE_TENDER(1,"投标中"),
    LOCKING(2,"锁定中"),
    FULL_SCALE(3,"满标"),
    LOAN_WATING(4,"放款中"),
    LOAN_ERROR(5,"放款失败"),
    LOAN_SUCCESS(6,"放款成功"),
//    LOAN_DISTRIBUTION_WATING(6,"放款未清处理中"),
    LOAN_DISTRIBUTION_SUCCESS(7,"放款清分成功"),
    LOAN_DISTRIBUTION_FAIL(8,"放款清分失败"),
    REFUND_WATING(9,"还款处理中"),
    RESOLVED(10,"已还款"),
    OVERDUE_RESOLVED(11,"逾期已还款"),
//    REFUND_DISTRIBUTION_WATING(11,"还款清分处理中"),
    REFUND_DISTRIBUTION_SUCCESS(12,"还款清分成功"),
    REFUND_DISTRIBUTION_ERROR(13,"还款清分失败")

    ;


    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private ScatterStandardStatusEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
