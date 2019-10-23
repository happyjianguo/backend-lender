package com.yqg.common.dynamicdata;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

import static com.yqg.common.config.JpaConfiguration.READ_DATASOURCE_KEY;
import static com.yqg.common.config.JpaConfiguration.WRITE_DATASOURCE_KEY;

/**
 * Created by gao on 2018/6/28.
 */
@Order(value = 1)
@Aspect
@Component
public class DynamicDataSourceAspect {

    @Pointcut("execution(public * com..*.service..*.*(..))")
    public void dataSourceSelector() {
    }

/*    @Around("dataSourceSelector()")
    public Object doAroundMethod(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();
        if (targetMethod.isAnnotationPresent(TargetDataSource.class)) {
            String targetDataSource = targetMethod.getAnnotation(TargetDataSource.class).dataSource();
            System.out.println("----------数据源是:" + targetDataSource + "------");
            DynamicDataSourceHolder.setDataSource(targetDataSource);
        }
        Object result = pjp.proceed();//执行方法
        DynamicDataSourceHolder.clearDataSource();

        return result;
    }*/

    /*
  * @Before("@annotation(ds)")
  * 的意思是：
  *
  * @Before：在方法执行之前进行执行：
  * @annotation(targetDataSource)：
  * 会拦截注解targetDataSource的方法，否则不拦截;
  */
    @Before("dataSourceSelector()")
    public void changeDataSource(JoinPoint point) throws Throwable {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        if (targetMethod.isAnnotationPresent(Transactional.class)) {
            DynamicDataSourceHolder.setDataSource(WRITE_DATASOURCE_KEY);
        }else {
            DynamicDataSourceHolder.setDataSource(READ_DATASOURCE_KEY);
        }
    }

    @After("dataSourceSelector()")
    public void restoreDataSource(JoinPoint point) {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        if (targetMethod.isAnnotationPresent(Transactional.class)) {
            //方法执行完毕之后，销毁当前数据源信息，进行垃圾回收。
            DynamicDataSourceHolder.clearDataSource();
        }
    }
}
