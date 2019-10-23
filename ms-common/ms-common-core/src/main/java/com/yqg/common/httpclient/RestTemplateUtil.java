package com.yqg.common.httpclient;

import com.alibaba.fastjson.JSON;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 包装restTemplate
 * Created by gao on 2018/7/3.
 */
@Component
public class RestTemplateUtil {

    final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected RestTemplate restTemplate;

    private String getApiUrl(String serviceName, String apiPath){
        return new StringBuilder("http://").append(serviceName).append(apiPath).toString();
    }

    /**
     * post请求服务
     *
     * @param serviceName
     * @param apiPath
     * @param ro
     * @param <T>
     * @return
     */
    public <T> T callPostService(String serviceName, String apiPath, BaseSessionIdRo ro, Class<T> tClass) throws BusinessException {
        long startTime = System.currentTimeMillis();
        String apiUrl= getApiUrl( serviceName, apiPath);
        BaseResponse baseResponse = restTemplate.postForObject(apiUrl, ro, BaseResponse.class);
        logger.info("返回结果:{},耗时:{}s", JSON.toJSONString(baseResponse), (System.currentTimeMillis() - startTime) / 1000.000);
        if (baseResponse.isSuccess()) {
            return BeanCoypUtil.convertMap(tClass, (Map) baseResponse.getData());
        } else {
            throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR.setCustomMessage(baseResponse.getMessage()));
        }
    }

    /**
     * get请求服务
     *
     * @param serviceName
     * @param apiPath
     * @param ro
     * @param <T>
     * @return
     */
    public <T> T callGetService(String serviceName,String apiPath, BaseSessionIdRo ro) throws BusinessException {
        Map<String, String> paramsMap = BeanCoypUtil.convertBean(ro);
        long startTime = System.currentTimeMillis();
        String apiUrl= getApiUrl( serviceName, apiPath);
        BaseResponse<T> baseResponse = restTemplate.getForObject(apiUrl, BaseResponse.class, paramsMap);
        logger.info("返回结果:{},耗时:{}s", JSON.toJSONString(baseResponse), (System.currentTimeMillis() - startTime) / 1000.000);
        if (baseResponse.isSuccess()) {
            return baseResponse.getData();
        } else {
            throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR.setCustomMessage(baseResponse.getMessage()));
        }
    }


}
