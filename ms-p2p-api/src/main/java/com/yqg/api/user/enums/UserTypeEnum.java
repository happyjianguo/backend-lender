package com.yqg.api.user.enums;

/**
 *  用户类型
 * @author  zhaoruifeng
 */
public enum UserTypeEnum {
    NORMAL_ACCOUNT(0,"普通账户"),
    ESCROW_ACCOUNT(1,"托管账户"),
    INCOME_ACCOUNT(2,"收入账户"),
    SUPPER_INVESTORS(3,"超级投资人"),
    BRANCH_INVESTORS(4,"机构投资人")
    ;
    private Integer type;
    private String name;

    UserTypeEnum(Integer type, String name) {
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
