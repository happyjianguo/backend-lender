package com.yqg.common.core.aspct;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yqg.common.config.CommonConfig;
import com.yqg.common.core.annocation.NotPrintResultLog;
import com.yqg.common.core.annocation.TruncateResultLog;
import com.yqg.common.core.request.BaseRo;
import com.yqg.common.core.request.check.RoCheckRule;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.request.check.IRoCheckRule;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.redis.BaseRedisKeyEnums;
import com.yqg.common.redis.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Aspect, parameter validation, login validation, logging, interface aging
 *
 * @author gaohaiming
 */
@Aspect
@Component
public class ControllorAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    CommonConfig commonConfig;
    /**
     * Check rule chain
     */
    private List<IRoCheckRule> checkRuleList = new ArrayList<>();

    public ControllorAspect() {
        //Add default validation rule
        checkRuleList.add(new RoCheckRule());
    }

    @Pointcut("execution(public * com..*.controllor..*(..))")
    public void checkAndLog() {
    }


    @Before("checkAndLog()")
    public void doBefore(JoinPoint point) throws Exception {
        //TODO Login filter
    }

    @AfterReturning(returning = "object", pointcut = "checkAndLog()")
    public void doAfter(JoinPoint joinPoint, Object object) {

    }

    @Around("checkAndLog()")
    public Object doAroundMethod(ProceedingJoinPoint pjd) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Object[] params = pjd.getArgs();//Get request payload
        Signature signature = pjd.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\r\n——————————————————Start———————————————————————————————————————————");
        stringBuilder.append("\r\n Request URI=");
        stringBuilder.append(request.getRequestURI());
        stringBuilder.append("  method:");
        stringBuilder.append(request.getMethod());
        stringBuilder.append("  remoteAddr:");
        stringBuilder.append(request.getRemoteAddr());
        stringBuilder.append("  contentType:");
        stringBuilder.append(request.getContentType());

        BaseSessionIdRo baseSessionIdRo = null;
        long startTime = 0;
        long endTime = 0;
        Object result = null;
        try {
            for (int i = 0; i < params.length; i++) {
                stringBuilder.append("\r\n Payload #");
                stringBuilder.append(i + 1);
                stringBuilder.append(":");
                if (null != params[i]) {
                    stringBuilder.append(params[i].getClass().getSimpleName());
                    stringBuilder.append("=");
                    if(params[i] instanceof BaseRo) {
                        //Validate payload
                        this.reqestParamCheck((BaseRo)params[i]);
                    }

                    if (params[i] instanceof BaseSessionIdRo) {
                        //Only one object can inherit the BaseSessionIdRo parameter, otherwise it will overwrite, which may cause the session check to fail.
                        baseSessionIdRo = (BaseSessionIdRo) params[i];
                        if (!StringUtils.isEmpty(request.getRemoteAddr())) {  //Get the client IP address
                            baseSessionIdRo.setClientIp(request.getRemoteAddr());
                        }
                        stringBuilder.append(JSON.toJSONString(baseSessionIdRo, SerializerFeature.WRITE_MAP_NULL_FEATURES));

                    } else if (params[i] instanceof MultipartFile) {
                        MultipartFile file = (MultipartFile) params[i];
                        stringBuilder.append(file.getOriginalFilename());
                        stringBuilder.append(" ");
                        stringBuilder.append(file.getSize() / 1024);
                        stringBuilder.append("KB");
                    } else {
                        stringBuilder.append(JSON.toJSONString(params[i], SerializerFeature.WRITE_MAP_NULL_FEATURES));
                    }
                } else {
                    stringBuilder.append("Payload is empty");
                }
            }
            //Check login session（Verify without annotation）
            if (!targetMethod.isAnnotationPresent(NotNeedLogin.class)) {
                if (baseSessionIdRo == null) {
                    baseSessionIdRo = new BaseSessionIdRo();
                    baseSessionIdRo.setSessionId(request.getParameter("sessionId"));
                }
                this.loginCheck(baseSessionIdRo);
            }
            startTime = System.currentTimeMillis();
            result = pjd.proceed();//Execute method and retrieve the result
            endTime = System.currentTimeMillis();
        } catch (BusinessException bex) {
            result = bex.getExceptionEnum().getMessage();
            if (commonConfig.isI18nOpen()) {
                result = bex.getExceptionEnum().getMessageI18n() + "（" + result + "）";
            }
            throw bex;
        } catch (Exception ex) {
            result = ex.getMessage();
            throw ex;
        } finally {
            endTime = System.currentTimeMillis();
            if (!targetMethod.isAnnotationPresent(NotPrintResultLog.class)) {//Check print the response or not
                stringBuilder.append("\r\n Response: ");
                if (targetMethod.isAnnotationPresent(TruncateResultLog.class)) {//Check print the response or not
                    stringBuilder.append(String.format("%.200s ...truncated...", JSON.toJSONString(result, SerializerFeature.WRITE_MAP_NULL_FEATURES)));
                }
                else if (stringBuilder.length() > 2000) {
                    //Max response length is 2000 characters
                    stringBuilder.append(String.format("%.2000s ...truncated...", JSON.toJSONString(result, SerializerFeature.WRITE_MAP_NULL_FEATURES)));
                }
                else {
                    stringBuilder.append(JSON.toJSONString(result, SerializerFeature.WRITE_MAP_NULL_FEATURES));
                }
            }
            double excTime = (endTime - startTime) / 1000.0000;
            stringBuilder.append("\r\n——————————————————End—————————————Duration:");
            stringBuilder.append(excTime);
            stringBuilder.append("s—————————————————");

            if (!request.getRequestURI().contains("/healthcheck")) {
                logger.info(stringBuilder.toString());
            }
        }

        return result;

    }

    /**
     * Validate login session
     *
     * @param baseSessionIdRo
     * @throws BusinessException
     */
    private void loginCheck(BaseSessionIdRo baseSessionIdRo) throws BusinessException {
        String sessionId = baseSessionIdRo.getSessionId();
        if (StringUtils.isEmpty(sessionId)) {
            throw new BusinessException(BaseExceptionEnums.NOT_LOGIN);
        }
        // Query the user userId from the cache according to the sessionId
        String userId = redisUtil.get(BaseRedisKeyEnums.USER_SESSION_KEY.appendToDefaultKey(sessionId));
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(BaseExceptionEnums.NOT_LOGIN);
        } else {
            //Set expiration time
            redisUtil.set(BaseRedisKeyEnums.USER_SESSION_KEY.appendToDefaultKey(sessionId), userId);
        }

        baseSessionIdRo.setUserId(userId);
    }

    /**
     * Validate 
     */
    private void reqestParamCheck(BaseRo baseRo) throws Exception {

        for (IRoCheckRule IRoCheckRule : checkRuleList) {
            IRoCheckRule.doCheck(baseRo);
        }
    }
}
