package com.yqg.pay.enums;

/**
 * 1.购买债权  2.放款 3.前置服务费收入 .4.还款 5.投资回款 6.回款收入
 *  交易类型
 * @author  zhaoruifeng
 */
public enum TransTypeEnum {
    BUY_CREDITOR(1,"购买债权","P2P_BOND"),//购买债权
    LOAN(2,"放款","P2P"),//放款
    SERVICE_FEE(3,"前置服务费收入","PRE_SERVICE_FEE"),//前置服务费收入
    INCOME(4,"还款","P2P"),//还款
    INVESTMENT_PAYBACK(5,"投资回款","WITHDRAW_FUND"),//投资回款
    PAYBACK_INCOME(6,"回款收入","INTEREST_REVENUE"),//回款收入
    CHARGE(7,"活期充值","CHARGE"),//活期充值---目前只有超级投资人会有用到
    RECHARGE(8,"活期提现","RECHARGE"),//活期提现---目前只有超级投资人会有用到 2天用不完的钱自动提现
    BUY_FINACE(9,"购买理财","P2P_FINACE"),//购买理财
    Fee(10,"利息打回","INTERNAL_TRANSFER"),//散标利息打回
    DINGQIFEE(11,"定期利息打回","INTERNAL_TRANSFER"),//定期利息打回
    DINGQI_PAYBACK(12,"投资回款","WITHDRAW_FUND"),//定期投资回款
    DINGQI_ZHAIZHUAN(13,"定期债转给超级投资人","P2P_FINACE"),//债转给超级投资人
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
