import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.ro.MessageRo;
import com.yqg.api.user.useruser.ro.UserAuthBankStatusRo;
import com.yqg.api.user.useruser.ro.UserReq;
import com.yqg.api.user.useruser.ro.UserTypeSearchRo;
import com.yqg.api.user.useruser.UserServiceApi;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.order.service.third.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("UserService")
public class UserServiceImpl implements UserService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier(value = "remoteRestTemplate")
    protected RestTemplate remoteRestTemplate;
    
    @Autowired
    protected RestTemplateUtil restTemplateUtil;
    
    @Value("${lenderapiUrl}")
    private String lenderapiUrl;

    @Override
    public BaseResponse<UserBankAuthStatus> userAuthBankInfo(@RequestBody UserAuthBankStatusRo ro) throws BusinessException {
        return new BaseResponse<UserBankAuthStatus>().successResponse();
    }

    @Override
    public BaseResponse<UserAccountBo> userListByType(@RequestBody UserTypeSearchRo ro) throws BusinessException {
        return new BaseResponse<UserAccountBo>().successResponse();
    }

    @Override
    public BaseResponse<UserBo> findUserById(@RequestBody UserReq ro) throws BusinessException {
        return new BaseResponse<UserBo>().successResponse();
    }

    @Override
    public BaseResponse<UserBo> findOneByMobileOrId(@RequestBody UserReq ro) throws BusinessException {
        return new BaseResponse<UserBo>().successResponse();
    }

    @Override
    public BaseResponse<UserBo> findOneByMobileOrName(@RequestBody UserReq ro) throws BusinessException {
        logger.info("FindOneByMobileOrName");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        return new BaseResponse<UserBo>().successResponse(
            restTemplateUtil.callPostService(
                lenderapiUrl+UserServiceApi.serviceName, UserServiceApi.path_findUserById, entity, UserBo.class, true));  
    }

    @Override
    public BaseResponse<Object> addUserMessage(@RequestBody MessageRo ro) throws BusinessException {
        return new BaseResponse<Object>().successResponse();
    }

    protected JSONObject executeHttpPost(String url, Object param) throws IllegalAccessException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        //headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        Map<String, Object> objectMap = objectToMap(param);
        StringBuilder sb = new StringBuilder();
        List<String> keys = new ArrayList<String>(objectMap.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            sb.append(key).append("=");
            sb.append(objectMap.get(key));
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
        logger.info("Payload---------" + sb.toString());
        String urlStr = url + "?" + sb.toString();
        logger.info("URL------" + urlStr);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(new LinkedMultiValueMap<String, String>(), headers);
        ResponseEntity<String> response = this.remoteRestTemplate.exchange(urlStr, HttpMethod.POST, entity, String.class);
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        logger.info("Response----------" + jsonObject.toJSONString());
        return jsonObject;
    }
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }
}