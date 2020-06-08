package com.yqg.user.service.third.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.system.sysbankbasicinfo.SysBankBasicInfoServiceApi;
import com.yqg.api.system.sysbankbasicinfo.bo.SysBankBasicInfoBo;
import com.yqg.api.system.sysbankbasicinfo.ro.SysBankBasicInfoRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.user.service.third.SysBankBasicThirdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@Component
public class SysBankBasicThirdServiceImpl implements SysBankBasicThirdService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestTemplateUtil restTemplateUtil;

    @Value("${lenderapiUrl}")
    private String lenderapiUrl;

    @Override
    public BaseResponse<SysBankBasicInfoBo> bankInfoByCode(@RequestBody SysBankBasicInfoRo ro) throws BusinessException {
        logger.info("bankInfoByCode");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<SysBankBasicInfoBo>().successResponse(
                restTemplateUtil.callPostService(
                        lenderapiUrl, SysBankBasicInfoServiceApi.path_bankInfoByCode, entity, SysBankBasicInfoBo.class, true));
    }
}
