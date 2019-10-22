package com.yqg.api.user.enums;

/**
 *
 *  账户流水变动类型
 * @author  hyy
 */
public enum AccountTransTypeEnum {
    CURRENT_ADD(1,"余额账户+","CURRENT_ADD"),
    LOCK_TO_CURRENT(2,"冻结金额 转 余额","LOCK_TO_CURRENT"),
    CURRENT_TO_LOCK(3,"余额金额 转 冻结","CURRENT_TO_LOCK"),
    LOCK_ADD(4,"冻结账户+","LOCK_ADD"),
    LOCK_TO_INVEST(5,"冻结金额 转 在投","LOCK_TO_INVEST"),
    INVEST_SUB(6,"在投金额减","INVEST_SUB"),
    INVEST_TO_CURRENT(7,"在投金额 转 余额","INVEST_TO_CURRENT"),
    LOCK_SUB(8,"冻结金额减","LOCK_SUB"),
    CURRENT_SUB(9,"余额账户-","CURRENT_SUB"),

//    CHARGE_ACCOUNT_ADD(1,"充值账户+","CHARGE_ACCOUNT_ADD"),//账户余额+ 账户余额- 冻结+
//    CHARGE_ACCOUNT_SUB(2,"充值账户-","CHARGE_ACCOUNT_SUB"),//账户余额+ 账户余额- 冻结+
//    CHARGE_LOCK_ADD(3,"充值冻结+","CHARGE_LOCK_ADD"),//账户余额+ 账户余额- 冻结+
//
//    LOAN_LOCK_SUB(4,"放款冻结减","LOAN_LOCK_SUB"),//放款 冻结减 在投加
//    LOAN_INVEST_ADD(5,"放款在投加","LOAN_INVEST_ADD"),//放款 冻结减 在投加
//
//    LOAN_FAILED_INVEST_SUB(6,"放款失败在投减","LOAN_FAILED_INVEST_SUB"),//放款失败 在投减 余额加
//    LOAN_FAILED_ACCOUNT_ADD(7,"放款失败余额减","LOAN_FAILED_INVEST_SUB"),//放款失败 在投减 余额加
//
//    ORDER_FAILED_LOCK_SUB(8,"订单失败在投减","ORDER_FAILED_LOCK_SUB"),//放款失败 冻结减 余额加
//    ORDER_FAILED_ACCOUNT_ADD(9,"订单失败余额","ORDER_FAILED_ACCOUNT_ADD"),//放款失败 冻结减 余额加
//
//    SERVICE_ACCOUNT_ADD(10,"前置服务费账户余额+","SERVICE_ACCOUNT_ADD"),//账户余额+
//    PAYBACK_INVEST_SUB(11,"回款在投-","PAYBACK_INVEST_SUB"),//回款在投-
//    PAYBACK_ACCOUNT_ADD(12,"回款账户余额+","PAYBACK_ACCOUNT_ADD"),//账户余额+
//
//    BUY_ACCOUNT_SUB(13,"余额购买账户余额-","BUY_ACCOUNT_SUB"),//余额购买账户余额-  余额购买冻结+
//    BUY_LOCK_ADD(14,"余额购买冻结+","BUY_LOCK_ADD"),//余额购买账户余额-  余额购买冻结+
//    WITHDRAW_DEPOSIT(15,"用户提现","WITHDRAW_DEPOSIT"),//用户提现
    ;
    private Integer type;
    private String name;
    private String disburseType;

    AccountTransTypeEnum(Integer type, String name, String disburseType) {
        this.type = type;
        this.name=name;
        this.disburseType=disburseType;
    }



    public String getDisburseType() {
        return disburseType;
    }

    public void setDisburseType(String disburseType) {
        this.disburseType = disburseType;
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
