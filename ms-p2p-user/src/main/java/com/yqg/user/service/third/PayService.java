package com.yqg.user.service.third;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.PayServiceApi;
import com.yqg.api.pay.loan.ro.LoanRo;
import com.yqg.api.pay.loan.vo.LoanResponse;
import com.yqg.api.pay.payaccounthistory.PayAccountHistoryServiceApi;
import com.yqg.api.pay.payaccounthistory.bo.PayAccountHistoryBo;
import com.yqg.api.pay.payaccounthistory.ro.PayAccountHistoryRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.service.third.impl.PayServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @Author: hyy
 * @Date: 2019/5/21 11:32
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
//@FeignClient(value = PayServiceApi.serviceName, fallback = PayServiceImpl.class)
public interface PayService {

    //提现
//    @PostMapping(value = PayServiceApi.path_loan)
    public BaseResponse<JSONObject> loan(LoanRo loanRo) throws Exception;

    //提现结果查询
//    @PostMapping(value = PayServiceApi.path_queryLoanResult)
    public BaseResponse<LoanResponse> queryLoanResult(LoanRo loanRo) throws BusinessException;

    //资金列表查询
//    @PostMapping(value = PayAccountHistoryServiceApi.path_getPayAccountHistoryByType)
    public List<PayAccountHistoryBo> getPayAccountHistoryByType(PayAccountHistoryRo ro) throws BusinessException;
}