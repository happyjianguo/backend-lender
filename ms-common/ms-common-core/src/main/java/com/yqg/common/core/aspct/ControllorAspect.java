package com.yqg.common.core.aspct;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yqg.common.config.CommonConfig;
import com.yqg.common.core.annocation.NotPrintResultLog;
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
import java.util.List;

/**
 * 切面,参数校验、登录验证、记录日志、接口时效
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
     * 检查规则链
     */
    private List<IRoCheckRule> checkRuleList = new ArrayList<>();

    public ControllorAspect() {
        //加入默认检查规则
        checkRuleList.add(new RoCheckRule());
    }

    @Pointcut("execution(public * com..*.controllor..*(..))")
    public void checkAndLog() {
    }


    @Before("checkAndLog()")
    public void doBefore(JoinPoint point) throws Exception {
        //TODO 登录过滤
    }

    @AfterReturning(returning = "object", pointcut = "checkAndLog()")
    public void doAfter(JoinPoint joinPoint, Object object) {

    }

    @Around("checkAndLog()")
    public Object doAroundMethod(ProceedingJoinPoint pjd) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Object[] params = pjd.getArgs();//获取请求参数
        Signature signature = pjd.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\r\n——————————————————开始———————————————————————————————————————————");
        stringBuilder.append("\r\n请求信息摘要: requestURI=");
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
            //参数校验
            for (int i = 0; i < params.length; i++) {
                stringBuilder.append("\r\n收到的参数");
                stringBuilder.append(i + 1);
                stringBuilder.append(":");
                if (null != params[i]) {
                    stringBuilder.append(params[i].getClass().getSimpleName());
                    stringBuilder.append("=");
                    if(params[i] instanceof BaseRo) {
                        //参数校验
                        this.reqestParamCheck((BaseRo)params[i]);
                    }

                    if (params[i] instanceof BaseSessionIdRo) {
                        //请求只能有一个对象继承了BaseSessionIdRo参数,否则会覆盖,可能导致session校验失败
                        baseSessionIdRo = (BaseSessionIdRo) params[i];
                        if (!StringUtils.isEmpty(request.getRemoteAddr())) {  //获取客户端ip地址
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
                    stringBuilder.append("null,收到参数为空");
                }
            }
            //登录session校验（没有注解则校验）
            if (!targetMethod.isAnnotationPresent(NotNeedLogin.class)) {
                if (baseSessionIdRo == null) {
                    baseSessionIdRo = new BaseSessionIdRo();
                    baseSessionIdRo.setSessionId(request.getParameter("sessionId"));
                }
                this.loginCheck(baseSessionIdRo);
            }
            //上面代码为方法执行前
            startTime = System.currentTimeMillis();//开始时间
            result = pjd.proceed();//执行方法，获取返回参数
            endTime = System.currentTimeMillis();//结束时间
            //下面代码为方法执行后
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
            endTime = System.currentTimeMillis();//结束时间
            if (!targetMethod.isAnnotationPresent(NotPrintResultLog.class)) {//是否打印结果
                stringBuilder.append("\r\n返回结果为:");
                stringBuilder.append(JSON.toJSONString(result, SerializerFeature.WRITE_MAP_NULL_FEATURES));
            }
            double excTime = (endTime - startTime) / 1000.0000;
            stringBuilder.append("\r\n——————————————————结束—————————————执行耗时:");
            stringBuilder.append(excTime);
            stringBuilder.append("s—————————————————");
            logger.info(stringBuilder.toString());
        }

        return result;

    }

    /**
     * 登录session校验
     *
     * @param baseSessionIdRo
     * @throws BusinessException
     */
    private void loginCheck(BaseSessionIdRo baseSessionIdRo) throws BusinessException {
        String sessionId = baseSessionIdRo.getSessionId();
        if (StringUtils.isEmpty(sessionId)) {
            throw new BusinessException(BaseExceptionEnums.NOT_LOGIN);
        }
        // 根据sessionId从缓存中查询用户userId
        String userId = redisUtil.get(BaseRedisKeyEnums.USER_SESSION_KEY.appendToDefaultKey(sessionId));
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(BaseExceptionEnums.NOT_LOGIN);
        } else {
            //延长失效时间
            redisUtil.set(BaseRedisKeyEnums.USER_SESSION_KEY.appendToDefaultKey(sessionId), userId);
        }

        baseSessionIdRo.setUserId(userId);
    }

    /**
     * 请求参数检查
     */
    private void reqestParamCheck(BaseRo baseRo) throws Exception {

        for (IRoCheckRule IRoCheckRule : checkRuleList) {
            IRoCheckRule.doCheck(baseRo);
        }
    }
}
