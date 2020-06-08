package com.yqg.task.service.impl;

import com.yqg.api.order.OrderOrderServiceApi;
import com.yqg.api.user.useraccount.UserAccountServiceApi;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.task.service.OrderTaskService;
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
public class OrderTaskServiceImpl implements OrderTaskService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestTemplateUtil restTemplateUtil;

    @Value("${lenderapiUrl}")
    private String lenderapiUrl;

    @Override
    public BaseResponse path_batchLoan() throws Exception {
        logger.info("path_batchLoan");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, OrderOrderServiceApi.path_batchLoan, entity, BaseResponse.class, true));
    }

    @Override
    public BaseResponse path_expireOrder() throws Exception {
        logger.info("path_expireOrder");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, OrderOrderServiceApi.path_expireOrder, entity, BaseResponse.class, true));
    }

    @Override
    public BaseResponse batchLoanWating() throws Exception {
        logger.info("batchLoanWating");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, OrderOrderServiceApi.path_batchLoanWating, entity, BaseResponse.class, true));
    }
}
