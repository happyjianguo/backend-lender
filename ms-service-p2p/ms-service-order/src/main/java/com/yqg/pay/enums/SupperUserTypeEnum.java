package com.yqg.pay.enums;

/**
 * 1.超级投资人,2.资金托管账户,3.收入账户
 *  超级用户类型
 * @author  zhaoruifeng
 */
public enum SupperUserTypeEnum {
    SUPPER_INVESTORS(1,"超级投资人"),
    ESCROW_ACCOUNT(2,"托管账户"),
    INCOME_ACCOUNT(3,"前置服务费收入"),
    INTEREST_ACCOUNT(4,"利息账户")// 是userType=4
    ;
    private Integer type;
    private String name;

    SupperUserTypeEnum(Integer type, String name) {
        this.type = type;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
