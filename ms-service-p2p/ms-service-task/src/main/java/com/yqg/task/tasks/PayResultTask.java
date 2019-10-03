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
public class PayResultTask extends AbstractTask {

    @Autowired
    private OrderTaskService orderTaskService;

    public PayResultTask() {
        this.setInervalTime(60);
        this.setDefaultStart(true);
        this.setForcedCancelAble(true);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.FIX_RATE_TASK;
    }

    @Override
    public void runTask(Object taskData)  {
        logger.info("执行支付结果定时任务:" + taskData);
        try {
            orderTaskService.payResult();
        } catch (BusinessException e) {
            e.printStackTrace();
        }

    }
}
