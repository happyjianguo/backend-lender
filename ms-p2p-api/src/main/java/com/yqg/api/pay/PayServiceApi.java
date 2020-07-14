package com.yqg.api.pay;

/**
 * Created by liuhuanhuan on 2018/9/3.
 */
public class PayServiceApi {
    public static final String serviceName = "service-pay";

    public static final String path_healthcheck = "/api-pay/healthcheck";
    //充值
    public static final String path_charge = "/api-pay/p2p/pay/income/charge";
    //立即投资--购买债权
    public static final String path_incomeRequest = "/api-pay/p2p/pay/income/incomeRequest";
    //立即投资--购买理财
    public static final String path_immediateInvestmentFinancing = "/api-pay/p2p/pay/income/immediateInvestmentFinancing";
    //还款
    public static final String path_repayment = "/api-pay/p2p/pay/income/repayment";
    //入账通知
    public static final String path_incomeNotify = "/api-pay/p2p/pay/income/incomeNotify";
    //payResult
    public static final String path_payResult = "/api-pay/p2p/pay/payResult";
    //buildPackage
    public static final String path_buildPackage = "/api-pay/p2p/pay/buildPackage";
    //buyPackage
    public static final String path_buyPackage = "/api-pay/p2p/pay/buyPackage";
    //superBuyPackage
    public static final String path_superBuyPackage = "/api-pay/p2p/pay/superBuyPackage";



    //打款
    public static final String path_loan = "/api-pay/p2p/pay/loan/payment";
    //打款结果查询
    public static final String path_queryLoanResult = "/api-pay/p2p/pay/loan/queryLoanResult";
    public static final String path_loanResult = "/api-pay/p2p/pay/loan/loanResult";
    //打款处理中
    public static final String path_loan_waiting = "/api-pay/p2p/pay/loan/waiting";
    public static final String path_refundClarify = "/api-pay/p2p/pay/loan/refundClarify";
    public static final String path_refundWatingTask = "/api-pay/p2p/pay/loan/refundWatingTask";

    //计算理财用户回款金额
    public static final String path_calculationBackAmount = "/api-pay/p2p/pay/back/calculationBackAmount";
    //理财用户利息回款waiting
    public static final String path_backInterestAmountWating = "/api-pay/p2p/pay/back/backInterestAmountWating";
    //给理财用户回款
    public static final String path_backAmount = "/api-pay/p2p/pay/back/backAmount";
    //理财用户回款waiting
    public static final String path_backAmountWating = "/api-pay/p2p/pay/back/backAmountWating";
}
