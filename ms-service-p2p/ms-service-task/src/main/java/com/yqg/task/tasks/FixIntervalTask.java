package com.yqg.task.tasks;

import com.yqg.task.enums.TaskType;
import com.yqg.task.service.AbstractTask;
import org.springframework.stereotype.Component;

/**
 * 固定时间间隔任务
 * Created by gao on 2018/7/17.
 */
@Component
public class FixIntervalTask extends AbstractTask {

    public FixIntervalTask() {
        this.setInervalTime(15);
        this.setDefaultStart(false);
        this.setForcedCancelAble(true);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.FIX_RATE_TASK;
    }

    @Override
    public void runTask(Object taskData)  {
        logger.info("执行定时任务:" + taskData);
        int i = 0;
        while (true) {
            System.out.println("id=::" + i++);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.info("执行中停止定时任务:" + taskData);
                break;
            }

            if (i == 30) break;
        }

    }
}
