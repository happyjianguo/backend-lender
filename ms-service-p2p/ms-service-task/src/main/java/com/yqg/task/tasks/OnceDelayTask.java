package com.yqg.task.tasks;

import com.yqg.task.enums.TaskType;
import com.yqg.task.service.AbstractTask;
import org.springframework.stereotype.Component;

/**
 * 一次性延迟任务
 * Created by gao on 2018/7/11.
 */
//@Component
public class OnceDelayTask extends AbstractTask {

    public OnceDelayTask() {
        this.setDelayTime(30);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.ONCE_DELAY_TASK;
    }


    @Override
    public void runTask(Object taskData) {
        logger.info("执行延时任务:" + taskData);
        try {
            Thread.sleep(30*1000L);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
