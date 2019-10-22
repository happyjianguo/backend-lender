package com.yqg.api.user.enums;

public enum StudentLoanStatusEnum {
    NOT_SUBMIT(0),  //0待提交,1待审核,2.审核通过,3.审核不通过
    NOT_CHECK(1),
    PASS(2),
    REFUSE(3);

    private Integer type;

    StudentLoanStatusEnum(Integer type){this.type = type;}

    public Integer getType(){return type;}

    public void setType(Integer type) {this.type = type;}
}
