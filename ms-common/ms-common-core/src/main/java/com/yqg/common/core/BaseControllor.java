package com.yqg.common.core;

import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.common.redis.BaseRedisKeyEnums;
import com.yqg.common.redis.RedisUtil;
import com.yqg.common.sender.CaptchaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

/**
 * 基础的controller 提供常用的方法
 * Created by gao on 2017/12/20.
 */
@Component
public abstract class BaseControllor {

    @Value("${spring.application.name}")
    private String serviceName;

    /**
     * 日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 缓存工具
     */
    @Autowired
    protected RedisUtil redisUtil;
    /**
     * 验证码工具
     */
    @Autowired
    protected CaptchaUtils captchaUtils;

    /**
     * 验证码工具
     */
    @Autowired
    protected RestTemplateUtil restTemplateUtil;

    /**
     * 默认成功返回值
     *
     * @return
     */
    protected BaseResponse successResponse() {

        return new BaseResponse().successResponse();
    }


    /**
     * 类创建完成后,初始化操作
     */
    @PostConstruct
    public void checkConfig() {
        Class clazz = this.getClass();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            //权限检查
            if (clazz.isAnnotationPresent(NotNeedLogin.class)) {
                configCheck(declaredMethod, BaseRedisKeyEnums.FILTER_NOT_CHECK_AUTH_KEY, true);
            } else if (declaredMethod.isAnnotationPresent(NotNeedLogin.class)) {
                configCheck(declaredMethod, BaseRedisKeyEnums.FILTER_NOT_CHECK_AUTH_KEY, true);
            } else {
                configCheck(declaredMethod, BaseRedisKeyEnums.FILTER_NOT_CHECK_AUTH_KEY, false);
            }
        }
    }

    private void configCheck(Method declaredMethod, BaseRedisKeyEnums keyEnums, boolean addConfig) {
        String apiPath = null;
        if (declaredMethod.isAnnotationPresent(PostMapping.class)) {
            PostMapping annotation = declaredMethod.getAnnotation(PostMapping.class);
            apiPath = serviceName + annotation.value()[0];

        } else if (declaredMethod.isAnnotationPresent(GetMapping.class)) {
            GetMapping annotation = declaredMethod.getAnnotation(GetMapping.class);
            apiPath = serviceName + annotation.value()[0];
        }
        if (!StringUtils.isEmpty(apiPath)) {
            if (addConfig) {
                redisUtil.setAdd(keyEnums, apiPath);
            } else {
                redisUtil.setRemove(keyEnums, apiPath);
            }
        }
    }
}
