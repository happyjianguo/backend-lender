package com.yqg.user.enums;

public enum UserAuthStatusEnum {
    NOT_SUBMIT(0),  //0.未实名 1.未通过 2.已实名 3.后台操作实名 4.人工审核拒绝
    NOT_PASS(1),
    PASS(2),
    MANAGE_PASS(3),
    REFUSE(4);

    private Integer type;

    UserAuthStatusEnum(Integer type){this.type = type;}

    public Integer getType(){return type;}

    public void setType(Integer type) {this.type = type;}
}
