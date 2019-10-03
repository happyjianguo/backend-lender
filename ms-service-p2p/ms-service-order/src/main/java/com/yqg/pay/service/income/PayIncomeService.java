package com.yqg.pay.service.income;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.income.ro.InvestmentFinancingRo;
import com.yqg.api.pay.income.ro.InvestmentRo;
import com.yqg.common.exceptions.BusinessException;

import java.math.BigDecimal;

public interface PayIncomeService {
    /**
     * 超级投资人充值
     *
     * @param amount
     * @throws BusinessException
     */
    public JSONObject charge(String userUuid,BigDecimal amount) throws BusinessException, IllegalAccessException;
    /**
     * 立即下单--散标
     * @param investmentRo
     * @throws BusinessException
     */
    public JSONObject immediateInvestment(InvestmentRo investmentRo) throws BusinessException, IllegalAccessException;

    /**
     * 立即下单 --理财
     * @param investmentFinancingRo
     * @return
     * @throws BusinessException
     */
    public JSONObject immediateInvestmentFinancing(InvestmentFinancingRo investmentFinancingRo) throws BusinessException, IllegalAccessException;
    /**
     * 立即还款
     * @param investmentRo
     * @throws BusinessException
     */
    public JSONObject repayment(InvestmentRo investmentRo) throws BusinessException, IllegalAccessException;
//    /**
//     * 入账通知
//     * @param investmentRo
//     * @throws BusinessException
//     */
//    public BaseResponse incomeNotify(InvestmentRo investmentRo) throws BusinessException;

    //托管账户 入账 购买债权  还款 生成付款码
    public JSONObject incomeRequest(String tradeNo, BigDecimal amount, String userName, String depositType) throws BusinessException, IllegalAccessException;

    //确认入账 托管账户 入账 购买债权  还款
    //收入账户确认入账  前置服务费 息差 滞纳金 只看转出确认就好不走这里 此处只针对托管账户
    public JSONObject incomeRequestQuery(String tradeNo) throws BusinessException;

    //定时任务 调用 付款结果
    public void payResult() throws Exception;
    //付款成功后  查询初始化债权包
//    public Creditorpackage initPackage(String productId) throws Exception;
    //债权打包
    public void buildPackage() throws BusinessException ;

    //buy债权包
    public void buyPackage() throws Exception;

    /**
     * 超级投资人购买 定时任务
     *
     * @throws BusinessException
     */
    public void superBuyPackage() throws BusinessException, IllegalAccessException;

}