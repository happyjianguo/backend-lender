package com.yqg.user.service.third.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.util.JSON;
import com.yqg.api.pay.PayServiceApi;
import com.yqg.api.pay.loan.ro.LoanRo;
import com.yqg.api.pay.loan.vo.LoanResponse;
import com.yqg.api.pay.payaccounthistory.PayAccountHistoryServiceApi;
import com.yqg.api.pay.payaccounthistory.bo.PayAccountHistoryBo;
import com.yqg.api.pay.payaccounthistory.ro.PayAccountHistoryRo;
import com.yqg.api.system.sysbankbasicinfo.SysBankBasicInfoServiceApi;
import com.yqg.api.system.sysbankbasicinfo.bo.SysBankBasicInfoBo;
import com.yqg.api.user.useraccount.UserAccountServiceApi;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.user.service.third.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestTemplateUtil restTemplateUtil;

    @Value("${lenderapiUrl}")
    private String lenderapiUrl;

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
    public List<PayAccountHistoryBo> getPayAccountHistoryByType(PayAccountHistoryRo ro) throws BusinessException{
        // TODO need to confirm if this work
        logger.info("getPayAccountHistoryByType");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        BaseResponse<JSONArray> response = new BaseResponse<JSONArray>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, PayAccountHistoryServiceApi.path_getPayAccountHistoryByType, entity, JSONArray.class, true));
        return response.getData().toJavaList(PayAccountHistoryBo.class);
    }
}