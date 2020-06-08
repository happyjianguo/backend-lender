package com.yqg.order.service.third.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.PayServiceApi;
import com.yqg.api.pay.income.bo.IncomeBo;
import com.yqg.api.pay.income.ro.IncomeRo;
import com.yqg.api.pay.loan.ro.LoanRo;
import com.yqg.api.pay.loan.vo.LoanResponse;
import com.yqg.api.pay.payaccounthistory.PayAccountHistoryServiceApi;
import com.yqg.api.pay.payaccounthistory.bo.BankCodeBo;
import com.yqg.api.pay.payaccounthistory.ro.PayAccountHistoryRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.order.service.third.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Primary
@Component
public class PayServiceImpl implements PayService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestTemplateUtil restTemplateUtil;

    @Value("${lenderapiUrl}")
    private String lenderapiUrl;

    @Override
    public BaseResponse<IncomeBo> incomeRequest(@RequestBody IncomeRo incomeRo) throws BusinessException {
        logger.info("incomeRequest");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(incomeRo), headers);
        return new BaseResponse<IncomeBo>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, PayServiceApi.path_incomeRequest, entity, IncomeBo.class, true));
    }

    @Override
    public BaseResponse<JSONObject> loan(LoanRo loanRo) throws Exception {
        logger.info("loan");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(loanRo), headers);
        return new BaseResponse<JSONObject>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, PayServiceApi.path_loan, entity, JSONObject.class, true));
    }

    @Override
    public BaseResponse<LoanResponse> queryLoanResult(LoanRo loanRo) throws BusinessException {
        logger.info("queryLoanResult");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(loanRo), headers);
        return new BaseResponse<LoanResponse>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, PayServiceApi.path_queryLoanResult, entity, LoanResponse.class, true));
    }

    @Override
    public String paymentCodeByOrderNo(@RequestBody PayAccountHistoryRo payAccountHistoryRo) throws Exception {
        logger.info("paymentCodeByOrderNo");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(payAccountHistoryRo), headers);
        return new BaseResponse<BankCodeBo>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, PayAccountHistoryServiceApi.path_paymentCode, entity, BankCodeBo.class, true)).getData().getBankCode();
    }
}
