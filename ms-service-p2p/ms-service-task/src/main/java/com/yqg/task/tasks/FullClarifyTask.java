package com.yqg.task.tasks;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.task.enums.TaskType;
import com.yqg.task.service.AbstractTask;
import com.yqg.task.service.OrderTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 满标清分任务
 * Created by huanhuan on 2018/12/4.
 */
@Component
public class FullClarifyTask extends AbstractTask {

    @Autowired
    private OrderTaskService orderTaskService;

    public FullClarifyTask() {
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
        logger.info("满标清分watting定时任务:" + taskData);
        try {
            orderTaskService.fullClarify();
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
