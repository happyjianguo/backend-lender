package com.yqg.task.tasks;

import com.yqg.task.enums.TaskType;
import com.yqg.task.service.AbstractTask;
import com.yqg.task.service.OrderTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanWatingTask extends AbstractTask {
    @Autowired
    private OrderTaskService orderTaskService;

    public LoanWatingTask() {
        this.setInervalTime(60);
        this.setDefaultStart(true);
        this.setForcedCancelAble(true);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.FIX_RATE_TASK;
    }

    @Override
    public void runTask(Object taskData) {
        logger.info("放款wating定时任务:" + taskData);
        try {
            orderTaskService.batchLoanWating();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
