package com.yqg.pay.service.third.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.user.useraccount.UserAccountServiceApi;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.userbank.UserBankServiceApi;
import com.yqg.api.user.userbank.bo.UserBankBo;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.api.user.useruser.ro.UserAuthBankStatusRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.pay.service.third.UserBankService;
import com.yqg.pay.service.third.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public class UserBankServiceImpl implements UserBankService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Value("${lenderapiUrl}")
    private String lenderapiUrl;

    @Override
    public BaseResponse<UserBankBo> getUserBankInfo(@RequestBody UserBankRo ro) throws BusinessException {
        logger.info("getUserBankInfo");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<UserBankBo>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, UserBankServiceApi.path_getUserBankInfo, entity, UserBankBo.class, true));

    }
}
