package com.yqg.task.service.impl;

import com.yqg.api.pay.PayServiceApi;
import com.yqg.api.user.useraccount.UserAccountServiceApi;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.task.service.PayTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Primary
@Component
public class PayTaskServiceImpl implements PayTaskService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestTemplateUtil restTemplateUtil;

    @Value("${lenderapiUrl}")
    private String lenderapiUrl;

    @Override
    public BaseResponse payResult() throws Exception {
        logger.info("payResult");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, PayServiceApi.path_payResult, entity, BaseResponse.class, true));
    }

    @Override
    public BaseResponse loanResult() throws Exception {
        logger.info("loanResult");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, PayServiceApi.path_loanResult, entity, BaseResponse.class, true));
    }
}
