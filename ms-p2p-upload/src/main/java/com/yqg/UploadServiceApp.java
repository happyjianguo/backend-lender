package com.yqg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 用户服务启动类
 */
@SpringBootApplication
//@EnableEurekaClient
public class UploadServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(UploadServiceApp.class, args);
	}
}
