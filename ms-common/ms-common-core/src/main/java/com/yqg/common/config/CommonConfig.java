package com.yqg.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 国际化配置
 * Created by gao on 2019/5/13.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "framework.common")
public class CommonConfig {
    /**
     * 是否开启国际化,默认关闭
     */
    private boolean i18nOpen=false;
    /**
     * 验证码长度
     */
    private int captchaLength=6;
    /**
     * 是否发送短信验证码,默认开启
     */
    private boolean sendSmsCaptcha =true;


}
