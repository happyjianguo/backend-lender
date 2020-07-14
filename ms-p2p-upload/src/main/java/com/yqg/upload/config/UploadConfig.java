package com.yqg.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * 文件上传配置信息
 * Created by gao on 2018/6/29.
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "upload")
public class UploadConfig {
    /**
     * 存储的根目录
     */
    private String rootPath="";

    //缩略图宽
    private Integer thumbnailWidth = 100;
    //缩略图高
    private Integer thumbnailHeight = 80;
    /**
     * 单个文件最大
     */
    private String maxFileSize;
    /**
     * 上传总限制
     */
    private String maxRequestSize;

    private String host;
    private String downloadContractUrl;

    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(maxFileSize);
        factory.setMaxRequestSize(maxRequestSize);
        return factory.createMultipartConfig();
    }
}
