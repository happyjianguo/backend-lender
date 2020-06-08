package com.yqg.order.service.third.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.income.bo.IncomeBo;
import com.yqg.api.pay.income.ro.IncomeRo;
import com.yqg.api.pay.loan.ro.LoanRo;
import com.yqg.api.pay.loan.vo.LoanResponse;
import com.yqg.api.pay.payaccounthistory.ro.PayAccountHistoryRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.service.third.PayService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public class PayServiceFallbackImpl implements PayService {


    @Override
    public BaseResponse<IncomeBo> incomeRequest(@RequestBody IncomeRo incomeRo) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse<JSONObject> loan(LoanRo loanRo) throws Exception {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse<LoanResponse> queryLoanResult(LoanRo loanRo) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public String paymentCodeByOrderNo(@RequestBody PayAccountHistoryRo payAccountHistoryRo) throws Exception {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
