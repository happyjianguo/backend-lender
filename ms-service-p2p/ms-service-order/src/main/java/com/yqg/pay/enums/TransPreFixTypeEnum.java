package com.yqg.pay.enums;

/**
 *  支付流水号前缀
 * @author  zhaoruifeng
 */
public enum TransPreFixTypeEnum {
    LOAN(10),//放款
    INCOME(11)//收款
    ;
    private Integer type;

    TransPreFixTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
