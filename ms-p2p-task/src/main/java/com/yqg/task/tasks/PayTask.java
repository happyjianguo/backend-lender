package com.yqg.task.tasks;

import com.yqg.task.enums.TaskType;
import com.yqg.task.service.AbstractTask;
import com.yqg.task.service.PayTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayTask extends AbstractTask {

    @Autowired
    private PayTaskService payTaskService;

    public PayTask() {
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
        logger.info("支付结果定时任务:" + taskData);
        try {
            payTaskService.payResult();

            //Rizky : Disable loanResult() (disburse task for PRE_SERVICE_FEE and Branch Investor)
            // as service fee is handled by t+2 and Branch investor is not enabled(2020/05/07)
            // TODO might need too re enable later
//            payTaskService.loanResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
