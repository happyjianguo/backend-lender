package com.yqg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.Calendar;

/**
 * 订单服务启动类
 */
@Slf4j
@SpringBootApplication
@EnableKafka
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@EnableHystrixDashboard
public class OrderServiceApp {

    public static void main(String[] args) {

		SpringApplication.run(OrderServiceApp.class, args);
    }


    private static Integer termToDays(String term){
        if (term.endsWith("d")){
            String d = term.replaceAll("d", "");
            return Integer.parseInt(d);
        }else if (term.endsWith("m")){
            String m = term.replaceAll("m", "");
            int i = Integer.parseInt(m);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH,i);
            long day = (calendar.getTimeInMillis()-Calendar.getInstance().getTimeInMillis())/(1000*60*60*24);
            return (int) day;
        }
        return 0;
    }

}
