package com.yqg.common.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传配置信息
 * Created by gao on 2017/06/26.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "upload.server")
public class UploadCustomConfig {
    /**
     * 上传服务器域名/ip地址
     */
    private String uploadHost;
    /**
     * 文件上传服务地址
     */
    private String uploadFilePath;
    /**
     * base64图片上传地址
     */
    private String uploadBase64Path;
}
