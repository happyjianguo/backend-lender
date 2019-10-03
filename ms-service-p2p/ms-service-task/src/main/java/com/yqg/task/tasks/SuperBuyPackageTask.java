package com.yqg.task.tasks;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.task.enums.TaskType;
import com.yqg.task.service.AbstractTask;
import com.yqg.task.service.OrderTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 超级投资人购买债权包任务
 * Created by huanhuan on 2018/12/4.
 */
@Component
public class SuperBuyPackageTask extends AbstractTask {

    @Autowired
    private OrderTaskService orderTaskService;

    public SuperBuyPackageTask() {
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


        logger.info("执行超级投资人购买债权包任务:" + taskData);
        try {
            orderTaskService.superBuyPackage();
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("执行超级投资人购买债权包结束:" + taskData);

    }
}
