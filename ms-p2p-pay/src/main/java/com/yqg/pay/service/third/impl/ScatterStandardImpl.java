package com.yqg.pay.service.third.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.order.OrderOrderServiceApi;
import com.yqg.api.order.orderorder.ro.OrderSuccessRo;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.pay.service.third.ScatterStandardService;
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
public class ScatterStandardImpl implements ScatterStandardService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestTemplateUtil restTemplateUtil;

    @Value("${lenderapiUrl}")
    private String lenderapiUrl;

    @Override
    public BaseResponse repaySuccess(@PathVariable(value = "creditorNo") String creditorNo, OrderSuccessRo ro) throws BusinessException {
        logger.info("repaySuccess");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, OrderOrderServiceApi.path_repaySuccess + "/" + creditorNo, entity, BaseResponse.class, true));
    }

    @Override
    public BaseResponse repayFail(@PathVariable(value = "creditorNo") String creditorNo) throws BusinessException {
        logger.info("repayFail");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, OrderOrderServiceApi.path_repayFail + "/" + creditorNo, entity, BaseResponse.class, true));
    }

    @Override
    public BaseResponse serviceFeeSuccess(@PathVariable(value = "creditorNo") String creditorNo) throws Exception {
        logger.info("serviceFeeSuccess");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, OrderOrderServiceApi.path_serviceFeeSuccess + "/" + creditorNo, entity, BaseResponse.class, true));
    }

}
