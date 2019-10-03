package com.yqg.task.tasks;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.task.enums.TaskType;
import com.yqg.task.service.AbstractTask;
import com.yqg.task.service.OrderTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liyixing on 2019/2/20.
 * 金额计算完成，给理财用户回款
 */
@Component
public class LoanFlowStandardTask extends AbstractTask {

    @Autowired
    private OrderTaskService orderTaskService;

    public LoanFlowStandardTask() {
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
        logger.info("超级投资人购买流标定时任务:" + taskData);
        try {
            orderTaskService.loanFlowStandard();
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
