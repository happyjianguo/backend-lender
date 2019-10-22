package com.yqg.order.service.third;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.PayServiceApi;
import com.yqg.api.pay.income.ro.IncomeRo;
import com.yqg.api.pay.loan.ro.LoanRo;
import com.yqg.api.pay.loan.vo.LoanResponse;
import com.yqg.api.pay.payaccounthistory.PayAccountHistoryServiceApi;
import com.yqg.api.pay.payaccounthistory.ro.PayAccountHistoryRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.service.third.impl.PayServiceFallbackImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@FeignClient(value = PayServiceApi.serviceName, fallback = PayServiceFallbackImpl.class)
public interface PayService {
    @PostMapping(value = PayServiceApi.path_incomeRequest)
    public BaseResponse<JSONObject> incomeRequest(@RequestBody IncomeRo incomeRo) throws BusinessException;
    @PostMapping(value = PayServiceApi.path_loan)
    public BaseResponse<JSONObject> loan(LoanRo loanRo) throws Exception;

    @PostMapping(value = PayServiceApi.path_queryLoanResult)
    public BaseResponse<LoanResponse> queryLoanResult(LoanRo loanRo) throws BusinessException;

    @PostMapping(value = PayAccountHistoryServiceApi.path_paymentCode)
    public String paymentCodeByOrderNo(@RequestBody PayAccountHistoryRo payAccountHistoryRo) throws Exception;
}