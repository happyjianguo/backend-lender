package com.yqg.pay.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by zhaoruifeng on 2018/9/5.
 */
@Component
@ConfigurationProperties(prefix = "third.pay.income")
@Data
public class InComeConfig {

    @Value("${third.pay.X-AUTH-TOKEN}")
    public String payToken;

    public String depositUrl;
    public String depositConfirmUrl;
    public String depositChannel;
    public String depositMethod;
    public String currency;
}
