package com.yqg.common.enums;

/**
 *  支付流水号前缀
 * @author  zhaoruifeng
 */
public enum TransPreFixTypeEnum {
    LOAN(10),//放款
    INCOME(11),//收款
    TRANSFER(12),//托管账户内转账
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
