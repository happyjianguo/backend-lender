package com.yqg.pay.service.loan;

import com.yqg.api.pay.loan.ro.LoanRo;
import com.yqg.common.exceptions.BusinessException;

public interface PayLoanService {

    public void loan(LoanRo loanRo) throws BusinessException, IllegalAccessException;

    //定时任务 满标清分
    void fullScaleDistributionTask() ;




    //定时任务 打款处理中
    void loanWatingTask() ;

    //定时任务 释放资金 定期到期的资金(涉及债转)和搁置两天的资金
    void releaseFundsTask() ;

    //定时任务 还款清分
    void refundClarify() throws IllegalAccessException;

    //定时任务 还款处理中
    void refundWatingTask() throws BusinessException;

    //定时任务 计算用户回款金额
    void calculationBackAmount();

    //定时任务 理财用户利息wating和回款Wating的
    void backInterestAmountWating();

    //定时任务，给用户回款
    void backAmount();

    //定时任务，查询用户回款处理中
    void backAmountWating();
}
