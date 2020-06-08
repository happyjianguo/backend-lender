package com.yqg.task.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.user.useraccount.UserAccountServiceApi;
import com.yqg.api.user.useruser.UserServiceApi;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.task.service.UserAccountTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: hyy
 * @Date: 2019/5/22 9:51
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@Component
public class UserAccountTaskServiceImpl implements UserAccountTaskService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestTemplateUtil restTemplateUtil;

    @Value("${lenderapiUrl}")
    private String lenderapiUrl;

    @Override
    public BaseResponse autoWithdrawDeposit() throws Exception {
//        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
        logger.info("autoWithdrawDeposit");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserAccountServiceApi.path_autoWithdrawDeposit, entity, BaseResponse.class, true));
    }

    @Override
    public BaseResponse autoWithdrawDepositCheck() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return new BaseResponse<>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserAccountServiceApi.path_autoWithdrawDepositCheck, entity, BaseResponse.class, true));
    }
}