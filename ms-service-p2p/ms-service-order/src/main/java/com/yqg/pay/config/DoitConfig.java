package com.yqg.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/9/10.
 */
@Component
@ConfigurationProperties(prefix = "third.doitLoan.url")
@Data
public class DoitConfig {
    public String doItLoanUrl;
}
