package com.yqg.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "third.advance")
@Configuration
public class AdvanceConfig {
    private String accessKey;
    private String secretKey;
    private String apiHost;
    private String identityCheckApi;
}
