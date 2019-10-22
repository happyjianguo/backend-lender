package com.yqg.common.enums;

/**
 *  资金表支付状态
 * @author  zhaoruifeng
 */
public enum PayAccountStatusEnum {
    WATING(1),//支付处理中
    SUCCESS(2),//支付成功
    ERROR(3),//支付失败
    EXPIRED(4)//支付超时
    ;
    private Integer type;

    PayAccountStatusEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
