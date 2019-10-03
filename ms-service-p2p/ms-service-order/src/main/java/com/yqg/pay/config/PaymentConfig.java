package com.yqg.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 试着提交
 * Created by zhaoruifeng on 2018/9/5.
 */
@Component
@ConfigurationProperties(prefix = "third.pay.loan")
@Data
public class PaymentConfig {

    private String disburseUrl;
    private String disburseQueryUrl;
    private String disburseChannel;
    private String disburseMethod;
    private String currency;
}
