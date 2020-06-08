package com.yqg.pay.service.third.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.order.OrderOrderServiceApi;
import com.yqg.api.user.useraccount.UserAccountServiceApi;
import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeRo;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.pay.service.third.UserAccountService;
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
public class UserAccountServiceImpl implements UserAccountService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestTemplateUtil restTemplateUtil;

    @Value("${lenderapiUrl}")
    private String lenderapiUrl;

    @Override
    public BaseResponse addUserCurrentBlance(@RequestBody UserAccountChangeRo ro) throws BusinessException {
        logger.info("addUserCurrentBlance");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserAccountServiceApi.path_addUserCurrentBlance, entity, BaseResponse.class, true));
    }

    @Override
    public BaseResponse current2lock(@RequestBody UserAccountChangeRo ro) throws BusinessException {
        logger.info("current2lock");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserAccountServiceApi.path_subtractUserCurrentBlance, entity, BaseResponse.class, true));

    }

    @Override
    public BaseResponse lock2current(@RequestBody UserAccountChangeRo ro) throws BusinessException {
        logger.info("lock2current");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserAccountServiceApi.path_releaseUserCurrentLockBlance, entity, BaseResponse.class, true));

    }

    @Override
    public BaseResponse userCharge(@RequestBody UserAccountChangeRo ro) throws BusinessException {
        logger.info("userCharge");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserAccountServiceApi.path_userCharge, entity, BaseResponse.class, true));

    }
}
