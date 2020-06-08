package com.yqg.user.service.third.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.system.sysparam.SysParamServiceApi;
import com.yqg.api.system.sysparam.bo.SysParamBo;
import com.yqg.api.system.sysparam.ro.SysParamRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.user.service.third.SysParamService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service("sysParamService")
public class SysParamServiceImpl implements SysParamService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestTemplateUtil restTemplateUtil;

    @Value("${lenderapiUrl}")
    private String lenderapiUrl;

    @Override
    public BaseResponse<SysParamBo> sysValueByKey(@RequestBody SysParamRo ro) throws BusinessException {
        logger.info("sysValueByKey");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<SysParamBo>().successResponse(
            restTemplateUtil.callPostService(
                lenderapiUrl, SysParamServiceApi.path_sysParamValueBykey, entity, SysParamBo.class, true));

    }
}
