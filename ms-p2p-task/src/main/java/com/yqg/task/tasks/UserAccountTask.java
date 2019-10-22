package com.yqg.task.tasks;

import com.yqg.task.enums.TaskType;
import com.yqg.task.service.AbstractTask;
import com.yqg.task.service.UserAccountTaskService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: hyy
 * @Date: 2019/5/21 16:02
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@Log
@Component
public class UserAccountTask {
    @Autowired
    private UserAccountTaskService accountTaskService;

    @Scheduled(cron = "0 0 19 * * ?")
    public void autoWithDrawDeposit() {
        log.info("用户账户自动提现定时任务开始");

        try {
            accountTaskService.autoWithdrawDeposit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("用户账户自动提现定时任务结束");
    }

    @Scheduled(cron = "0 0/2 * * * ?")
    public void handWithDrawDeposit() {
        log.info("用户账户自动提现结果查询定时任务开始");

        try {
            accountTaskService.autoWithdrawDepositCheck();
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("用户账户自动提现结果查询定时任务结束");
    }

}