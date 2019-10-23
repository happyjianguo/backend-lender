package com.yqg.common.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * spring 上下文获取bean工具
 * Created by gao on 2017/9/10.
 */
@Component
@Lazy(false)
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext context;

    public static final String LOCAL_PROFILE = "local";//本地环境
    public static final String DEV_PROFILE = "dev";//开发环境
    public static final String TEST_PROFILE = "test";//测试环境
    public static final String PROD_PROFILE = "prod";//生产环境


    public static ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac)
            throws BeansException {
        context = ac;
    }

    public static <T> T getBean(Class<T> tClass) {
        if (context != null) {
            return context.getBean(tClass);
        }
        return null;
    }

    public static <T> T getBean(String name, Class<T> tClass) {
        return context.getBean(name, tClass);
    }

    // 获取当前环境
    public static String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }
    // 是否生产环境
    public static boolean isProdProfile() {
        return PROD_PROFILE.equals(context.getEnvironment().getActiveProfiles()[0]);
    }

}
