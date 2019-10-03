package com.yqg.order.enums;

/**
 * Created by huanhuan on 2018/9/5.
 */
public enum ScatterStandardStatusEnums {
    //1.投标中 2锁定中 3.满标  4.放款中 5.放款失败 6.放款成功(待还款)  7.还款处理中 8.已还款 9.还款清分处理中  10.还款清分处理成功 11.还款清分处理失败 12.流标
    THE_TENDER(1,"投标中"),
    LOCKING(2,"锁定中"),
    FULL_SCALE(3,"满标"),
    LENDING_WATING(4,"放款中"),
    LENDING_ERROR(5,"放款失败"),
    LENDING_SUCCESS(6,"放款成功"),
    REFUND_WATING(7,"还款处理中"),
    RESOLVED(8,"已还款"),
    REFUND_DISTRIBUTION_WATING(9,"还款清分处理中"),
    REFUND_DISTRIBUTION_SUCCESS(10,"还款清分成功"),
    REFUND_DISTRIBUTION_ERROR(11,"还款清分失败"),
    OLD_FLOW_STANDARD(12,"老流标数据--已废弃 12 保留不再用"),

    RE_MATCH(13,"重新匹配成功"),
    FLOW_STANDARD(15,"流标")
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
