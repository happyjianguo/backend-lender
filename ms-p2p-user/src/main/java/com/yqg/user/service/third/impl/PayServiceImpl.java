package com.yqg.user.service.third.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.loan.ro.LoanRo;
import com.yqg.api.pay.loan.vo.LoanResponse;
import com.yqg.api.pay.payaccounthistory.bo.PayAccountHistoryBo;
import com.yqg.api.pay.payaccounthistory.ro.PayAccountHistoryRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.service.third.PayService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: hyy
 * @Date: 2019/5/21 11:32
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@Component
public class PayServiceImpl implements PayService{
    @Override
    public BaseResponse<JSONObject> loan(LoanRo loanRo) throws Exception {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse<LoanResponse> queryLoanResult(LoanRo loanRo) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public List<PayAccountHistoryBo> getPayAccountHistoryByType(PayAccountHistoryRo ro) throws BusinessException{
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}