package com.yqg.user.enums;

public enum UserTypeEnum {      //0默认普通用户 1.超级投资人 2.资金托管账户 3.收入账户 4.利息账户 5.借款学生
    NORMAL_USER(0),
    SUPER_INVITE(1),
    MONEY_MANAGE(2),
    INCOME_ACCOUNT(3),
    INTEREST_ACCOUNT(4),
    STUDENT(5);

    private Integer type;

    UserTypeEnum(Integer type){this.type = type;}

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
