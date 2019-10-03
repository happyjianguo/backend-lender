package com.yqg.pay.enums;

/**
 *
 * 请求接口日志类型
 * @author  zhaoruifeng
 */
public enum ThirdLogTypeEnum {
    LOAN_REQUEST(1,"LOAN_REQUEST"),//请求放款
    LOAN_REQUEST_QUERY(2,"LOAN_REQUEST_QUERY"),//请求放款查询
    INCOME_REQUEST(3,"INCOME_REQUEST"),
    INCOME_REQUEST_QUERY(4,"INCOME_REQUEST_QUERY")
    ;
    private Integer type;
    private String name;

    ThirdLogTypeEnum(Integer type,String name) {
        this.type = type;
        this.name=name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
