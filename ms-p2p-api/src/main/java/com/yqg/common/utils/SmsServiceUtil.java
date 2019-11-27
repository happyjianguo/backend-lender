package com.yqg.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.yqg.common.config.CommonConfig;
import com.yqg.common.context.ApplicationContextProvider;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Log4j
public class SmsServiceUtil {
    @Autowired
    @Qualifier(value = "smsRestTemplate")
    protected RestTemplate smsRestTemplate;
    
    @Autowired
    CommonConfig commonConfig;

    @Bean(name = "smsRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Value("${HttpUrl.smsUrl}")
    private String smsUrl;

    @Value("${SmsChannelType.type}")
    private String channelType;

    @Value("${HttpUrl.smsLoginCodeUrl}")
    private String smsLoginCodeUrl;

    @Value("${HttpUrl.smsVerifyUrl}")
    private String smsVerifyUrl;


    public void sendTypeSmsCode(String smsType, String mobileNumber,String content) 
    {
        if (!ApplicationContextProvider.isProdProfile() && !commonConfig.isSendSmsCaptcha()) {
            log.info("Non-production environment and configured not to send SMS, skip sending SMS to " + mobileNumber);
            return;
        }
        Map<String,String> map = new HashMap<String, String>();
        map.put("smsChannel",channelType);
        map.put("productType","DO_IT");
        map.put("smsTrigger",smsType);
        map.put("sendFrom","DO-IT");
        map.put("sendTo",mobileNumber);
        map.put("content",content);

        List<String> keys = new ArrayList<String>(map.keySet());
        StringBuilder sb = new StringBuilder();
        for(String key:keys){
            sb.append(key).append("=");
            sb.append(map.get(key));
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);


        String urlStr=smsUrl+"?"+sb.toString();
        log.info("Request URL------" + urlStr);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("X-AUTH-TOKEN", "doit");


        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(new LinkedMultiValueMap<String, String>(), headers);
        JSONObject jsonObject = smsRestTemplate.postForObject(urlStr, entity, JSONObject.class);
        log.info("result=>" + !jsonObject.containsKey("code"));
        log.info("result=>" + !jsonObject.getString("code").equals("0"));
        log.info("Response: " + jsonObject);

    }


    public void sendSmsLoginCode(String mobileNumber) {
        if (!ApplicationContextProvider.isProdProfile() && !commonConfig.isSendSmsCaptcha()) {
            log.info("Non-production environment and configured not to send SMS, skip sending SMS to " + mobileNumber);
            return;
        }
        
        Map<String,String> map = new HashMap<String, String>();
        map.put("productType","DO_IT");
        map.put("via","sms");
        map.put("sendTo",mobileNumber);

        List<String> keys = new ArrayList<String>(map.keySet());
        StringBuilder sb = new StringBuilder();
        for(String key:keys){
            sb.append(key).append("=");
            sb.append(map.get(key));
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);


        String urlStr=smsLoginCodeUrl+"?"+sb.toString();
        log.info("Request URL------" + urlStr);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("X-AUTH-TOKEN", "doit");


        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(new LinkedMultiValueMap<String, String>(), headers);
        JSONObject jsonObject = smsRestTemplate.postForObject(urlStr, entity, JSONObject.class);
        log.info("result=>" + !jsonObject.containsKey("code"));
        log.info("result=>" + !jsonObject.getString("code").equals("0"));
        log.info("Response: " + jsonObject);
    }


    /**
     * 校验登录验证码接口
     * @param mobileNumber
     * @param code
     * @throws BusinessException
     */
    public void smsVerifyCode(String mobileNumber,String code) throws BusinessException {
        if (!ApplicationContextProvider.isProdProfile() && !commonConfig.isSendSmsCaptcha()) {
            log.info("Non-production environment and configured not to send SMS, skip sending SMS to " + mobileNumber);
            return;
        }

        Map<String,String> map = new HashMap<String, String>();
        map.put("via","sms");
        map.put("sendTo",mobileNumber);
        map.put("code",code);

        List<String> keys = new ArrayList<String>(map.keySet());
        StringBuilder sb = new StringBuilder();
        for(String key:keys){
            sb.append(key).append("=");
            sb.append(map.get(key));
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);


        String urlStr=smsVerifyUrl+"?"+sb.toString();
        log.info("Request URL------" + urlStr);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("X-AUTH-TOKEN", "doit");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(new LinkedMultiValueMap<String, String>(), headers);
        JSONObject jsonObject = smsRestTemplate.postForObject(urlStr, entity, JSONObject.class);
        if(!"0".equals(jsonObject.getString("code"))){
            throw new BusinessException(BaseExceptionEnums.CPPCHA_ERROR);
        }
        log.info("result=>" + !jsonObject.containsKey("code"));
        log.info("result=>" + !jsonObject.getString("code").equals("0"));
        log.info("Response: " + jsonObject);

    }
}
