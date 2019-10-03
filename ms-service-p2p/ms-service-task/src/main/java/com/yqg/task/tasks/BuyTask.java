package com.yqg.task.tasks;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.task.enums.TaskType;
import com.yqg.task.service.AbstractTask;
import com.yqg.task.service.OrderTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 支付结果查询任务
 * Created by huanhuan on 2018/12/4.
 */
@Component
public class BuyTask extends AbstractTask {

    @Autowired
    private OrderTaskService orderTaskService;

    public BuyTask() {
        this.setInervalTime(60);
        this.setDefaultStart(false);
        this.setForcedCancelAble(true);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.FIX_RATE_TASK;
    }

    @Override
    public void runTask(Object taskData) {
       logger.info("6小时流标开始:" + taskData);
        try {
            orderTaskService.deal6HoursLaterOrder();
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        logger.info("6小时流标结束:" + taskData);

/*
        logger.info("执行过期订单任务:" + taskData);
        try {
            orderTaskService.deal30MinLaterOrder();
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        logger.info("执行过期订单任务结束:" + taskData);
*/
    }
}
