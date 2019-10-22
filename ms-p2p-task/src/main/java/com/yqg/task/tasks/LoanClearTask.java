package com.yqg.task.tasks;

import com.yqg.task.enums.TaskType;
import com.yqg.task.service.AbstractTask;
import com.yqg.task.service.LoanClearTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Remark:
 * Created by huwei on 19.6.12.
 */
@Component
public class LoanClearTask extends AbstractTask {

    @Autowired
    private LoanClearTaskService loanClearTaskService;

    public LoanClearTask() {
//        this.setInervalTime(60);
        this.setCronTime("0 0 2 * * ?");
        this.setDefaultStart(true);
        this.setForcedCancelAble(true);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.CUSTOM_CORN_TASK;
    }

    @Override
    public void runTask(Object taskData) {
        logger.info("机构投资人清分处理定时任务:" + taskData);
        try {
            loanClearTaskService.handleBreachDisburse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
