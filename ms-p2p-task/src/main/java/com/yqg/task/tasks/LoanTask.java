package com.yqg.task.tasks;

import com.yqg.task.enums.TaskType;
import com.yqg.task.service.AbstractTask;
import com.yqg.task.service.OrderTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Remark:
 * Created by huwei on 19.5.21.
 */
@Component
public class LoanTask extends AbstractTask {
    @Autowired
    private OrderTaskService orderTaskService;

    public LoanTask() {
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
        logger.info("放款定时任务:" + taskData);
        try {
            orderTaskService.path_batchLoan();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
