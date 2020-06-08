package com.yqg.pay.service.third.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.userbank.UserBankServiceApi;
import com.yqg.api.user.useruser.UserServiceApi;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.ro.UserAuthBankStatusRo;
import com.yqg.api.user.useruser.ro.UserReq;
import com.yqg.api.user.useruser.ro.UserTypeSearchRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.pay.service.third.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Primary
@Service("UserService")
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestTemplateUtil restTemplateUtil;

    @Value("${lenderapiUrl}")
    private String lenderapiUrl;

    @Override
    public BaseResponse<UserBankAuthStatus> userAuthBankInfo(UserAuthBankStatusRo ro) throws BusinessException {
        logger.info("userAuthBankInfo");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<UserBankAuthStatus>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserServiceApi.path_userAuthBankStatus, entity, UserBankAuthStatus.class, true));
    }

    @Override
    public BaseResponse<UserAccountBo> userListByType(UserTypeSearchRo ro) throws BusinessException {
        logger.info("userListByType");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<UserAccountBo>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserServiceApi.path_userListByType, entity, UserAccountBo.class, true));
    }

    @Override
    public BaseResponse<UserBo> findUserById(UserReq ro) throws BusinessException {
        logger.info("findUserById");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<UserBo>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserServiceApi.path_findUserById, entity, UserBo.class, true));
    }

    @Override
    public BaseResponse<UserBo> findOneByMobileOrId(@RequestBody UserReq ro) throws BusinessException {
        logger.info("findOneByMobileOrId");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<UserBo>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserServiceApi.path_findUserByMobileOrId, entity, UserBo.class, true));
    }

    @Override
    public BaseResponse<UserBo> findOneByMobileOrName(@RequestBody UserReq ro) throws BusinessException {
        logger.info("FindOneByMobileOrName");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<UserBo>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserServiceApi.path_findOneByMobileOrName, entity, UserBo.class, true));
    }
}
