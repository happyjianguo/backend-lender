package com.yqg.order.service.third.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.user.useraccount.UserAccountServiceApi;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useraccount.ro.UserAccountNotSessionRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.order.service.third.UserAccountService;
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

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Value("${lenderapiUrl}")
    private String lenderapiUrl;


    @Override
    public BaseResponse addCurrentBlance(@RequestBody UserAccountChangeRo ro) throws BusinessException {
        logger.info("addCurrentBlance");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<UserAccountBo>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserAccountServiceApi.path_addUserCurrentBlance, entity, UserAccountBo.class, true));
    }

    @Override
    public BaseResponse subtractLockedBlance(UserAccountChangeRo ro) throws BusinessException {
        logger.info("addCurrentBlance");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<UserAccountBo>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserAccountServiceApi.path_subtractUserAccountbLocked, entity, UserAccountBo.class, true));

    }

    @Override
    public BaseResponse current2lock(@RequestBody UserAccountChangeRo ro) throws BusinessException {
        logger.info("current2lock");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<UserAccountBo>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserAccountServiceApi.path_subtractUserCurrentBlance, entity, UserAccountBo.class, true));

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
    public BaseResponse lock2used(@RequestBody UserAccountChangeRo ro) throws BusinessException {
        logger.info("lock2used");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserAccountServiceApi.path_addUserAccountInvesting, entity, BaseResponse.class, true));
    }

    @Override
    public BaseResponse used2current(@RequestBody UserAccountChangeRo ro) throws Exception {
        logger.info("used2current");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserAccountServiceApi.path_addUserAccountForFailed, entity, BaseResponse.class, true));

    }

    @Override
    public BaseResponse<UserAccountBo> selectUserAccountNotSession(@RequestBody UserAccountNotSessionRo ro) throws BusinessException {
        logger.info("userAuthBankInfo");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<UserAccountBo>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserAccountServiceApi.path_selectUserAccountNotSession, entity, UserAccountBo.class, true));
    }
}
