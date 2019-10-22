package com.yqg.pay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RemoteRestTemplateConfig {

    @Bean(name = "remoteRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
