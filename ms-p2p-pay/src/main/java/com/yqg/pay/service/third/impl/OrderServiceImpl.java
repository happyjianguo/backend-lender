package com.yqg.pay.service.third.impl;

import com.yqg.api.order.OrderOrderServiceApi;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.pay.service.third.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Primary
@Component
public class OrderServiceImpl implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestTemplateUtil restTemplateUtil;

    @Value("${lenderapiUrl}")
    private String lenderapiUrl;

    @Override
    public BaseResponse successOrder(@PathVariable(value = "orderNo") String orderNo) throws BusinessException {
        logger.info("repaySuccess");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, OrderOrderServiceApi.path_successOrder + "/" + orderNo, entity, BaseResponse.class, true));
    }

    @Override
    public BaseResponse failOrder(@PathVariable(value = "orderNo") String orderNo) throws BusinessException {
        logger.info("repaySuccess");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, OrderOrderServiceApi.path_failOrder + "/" + orderNo, entity, BaseResponse.class, true));
    }
}
