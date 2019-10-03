package com.yqg.task.tasks;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.task.enums.TaskType;
import com.yqg.task.service.AbstractTask;
import com.yqg.task.service.OrderTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 购买债权包任务
 * Created by huanhuan on 2018/12/4.
 */
@Component
public class BuyPackageTask extends AbstractTask {

    @Autowired
    private OrderTaskService orderTaskService;

    public BuyPackageTask() {
        this.setInervalTime(60);
        this.setDefaultStart(false);
        this.setForcedCancelAble(true);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.FIX_RATE_TASK;
    }

    @Override
    public void runTask(Object taskData)  {
        logger.info("购买债权包任务:" + taskData);
        try {
            orderTaskService.buyPackage();
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("购买债权包任务结束:" + taskData);

    }
}
