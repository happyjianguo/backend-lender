package com.yqg.task.tasks;

import com.yqg.task.enums.TaskType;
import com.yqg.task.service.AbstractTask;
import org.springframework.stereotype.Component;

/**
 * 自定义表达任务
 * Created by gao on 2018/7/17.
 */
//@Component
public class CronTask extends AbstractTask {
    @Override
    public TaskType getTaskType() {
        return TaskType.CUSTOM_CORN_TASK;
    }

    public CronTask() {
        this.setCronTime("0/20 * * * * *");
    }

    @Override
    public String getCronTime() {
        return super.getCronTime();
    }

    @Override
    public void runTask(Object taskData) {
        logger.info("执行自定义任务:" + taskData);

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.info("执行中停止定时任务:" + taskData);
        }

    }
}
