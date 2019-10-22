package com.yqg.pay.service.income;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.income.ro.IncomeRo;
import com.yqg.common.exceptions.BusinessException;

public interface PayIncomeService {

    //托管账户 入账 购买债权  还款 生成付款码
//    public JSONObject incomeRequest(String tradeNo, BigDecimal amount, String userName, String depositType) throws BusinessException, IllegalAccessException;
    public JSONObject incomeRequest(IncomeRo incomeRo) throws BusinessException, IllegalAccessException;

    //确认入账 托管账户 入账 购买债权  还款
    //收入账户确认入账  前置服务费 息差 滞纳金 只看转出确认就好不走这里 此处只针对托管账户
    public JSONObject incomeRequestQuery(String tradeNo) throws BusinessException;

    //定时任务 调用 付款结果
    public void payResult() throws Exception;



}