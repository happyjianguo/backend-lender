package com.yqg.pay.enums;

/**
 * Created by liyixing on 2019/4/11.
 */
public enum CreditorPackageStatusEnum {
    BUY(1),//购买中
    BUY_FULL(2),//以买满
    ZHAIZHUAN(4)//债转
    ;
    private Integer type;

    CreditorPackageStatusEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
