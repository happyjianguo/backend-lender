package com.yqg.api.user.enums;

public enum UserBankCardBinEnum {
    NOT(0),
    PENDING(1),
    SUCCESS(2),
    FAILED(3),
    ;
    private int type;

    UserBankCardBinEnum(int  type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
