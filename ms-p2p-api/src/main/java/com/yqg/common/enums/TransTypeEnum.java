package com.yqg.common.enums;

/**
 * 1.购买债权  2.放款 3.前置服务费收入 .4.还款 5.投资回款 6.回款收入
 *  交易类型
 * @author  zhaoruifeng
 */
public enum TransTypeEnum {
    BUY_CREDITOR(1,"购买债权","P2P_BOND"),//购买债权
    BUY_CREDITOR_BRANCH(2,"购买债权","INTERNAL_TCC"),//购买债权
    LOAN(3,"放款","PAYDAYLOAN"),//放款
    LOAN_STAGING(10,"放款","LOANSTAGING"),
    SERVICE_FEE(4,"前置服务费收入","PRE_SERVICE_FEE"),//前置服务费收入   (砍头息-利息）*25%（25%可以配置）
    INCOME(5,"还款","PAYDAYLOAN"),//还款
//    INVESTMENT_PAYBACK(5,"投资回款","WITHDRAW_FUND"),//投资回款
    PAYBACK_INCOME(6,"回款(逾期)收入","INTEREST_REVENUE"),//回款收入
    RECHARGE(7,"活期提现","RECHARGE"),//活期提现
    CHARGE(8,"活期充值","CHARGE"),//活期充值---目前只有超级投资人会有用到
    BRANCH_CLEAR(9,"机构清分","BRANCH_CLEAR"),//机构清分--机构投资人清分收入分摊给超级投资人
    ;
    private Integer type;
    private String name;
    private String disburseType;

    TransTypeEnum(Integer type,String name,String disburseType) {
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
