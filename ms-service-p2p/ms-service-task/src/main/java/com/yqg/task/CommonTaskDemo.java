package com.yqg.task;

import com.yqg.task.service.OrderTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通常的任务
 * Created by gao on 2018/7/11.
 */
@Component
public class CommonTaskDemo {

    private static final Logger logger = LoggerFactory.getLogger(CommonTaskDemo.class);
    @Autowired
    private OrderTaskService payResultTaskService;
    //spring的schedule值支持6个域的表达式（秒 分 时 每月第几天 月 星期）不支持年
    // 具体参见 <https://blog.csdn.net/ninifengs/article/details/77141240>

   /* @Scheduled(cron = "0/120 * * * * *")
    public void task1(){
        logger.info("执行合买任务:" );
        try {
            payResultTaskService.deal6HoursLaterOrder();
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        logger.info("执行合买任务结束:" );



    }
    @Scheduled(cron = "0/120 * * * * *")
    public void task2(){
        logger.info("执行过期订单任务:" );
        try {
            payResultTaskService.deal30MinLaterOrder();
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        logger.info("执行过期订单任务结束:" );
    }*/
}
