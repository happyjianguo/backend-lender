package com.yqg.user.enums;

public enum StudentAddressTypeEnum {
    STUDENT_ID(0),
    STUDENT_LIVE(1),
    STUDENT_SCHOOL(2),
    GUARANTEE_ID(3),
    GUARANTEE_LIVE(4),
    GUARANTEE_COMPANY(5);

    private Integer type;

    StudentAddressTypeEnum(Integer type) { this.type = type; }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
