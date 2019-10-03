package com.yqg.task.service.impl;

import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.task.enums.TaskStatus;
import com.yqg.task.enums.TaskType;
import com.yqg.task.config.TaskConfig;
import com.yqg.task.service.AbstractTask;
import com.yqg.task.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.ScheduledFuture;

/**
 * 执行任务
 * Created by gaohaiming on 2017/8/10.
 */
@Component
public class TaskServiceImpl implements TaskService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ThreadPoolTaskScheduler taskPool;

    @Autowired
    protected TaskConfig taskConfig;

    /**
     * 任务列表
     *
     * @return
     */
    @Override
    public Collection<AbstractTask> taskList() {
        return TaskConfig.taskMap.values();
    }

    @Override
    public AbstractTask getTaskById(String taskId) throws BusinessException {
        AbstractTask task = TaskConfig.taskMap.get(taskId);
        if (null == task) {
            throw new BusinessException(BaseExceptionEnums.TASK_NO_FUND_ERROR);
        }
        return task;
    }

    /**
     * 添加任务
     *
     * @param taskId
     */
    @Override
    public void startTask(String taskId, Object taskData) throws BusinessException {
        AbstractTask task = this.getTaskById(taskId);
        task.setTaskStatus(TaskStatus.STARTING);
        ScheduledFuture<?> schedule = null;
        switch (task.getTaskType()) {

            case ONCE_DELAY_TASK:
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, task.getDelayTime());
                schedule = taskPool.schedule(new CommonTaskExcutor(taskId, task, taskData),
                        calendar.getTime());
                logger.info("添加延迟任务:{},在{}秒后执行", taskId, task.getDelayTime());
                break;
            case FIX_RATE_TASK:
                schedule = taskPool.scheduleAtFixedRate(
                        new CommonTaskExcutor(taskId, task, taskData), task.getInervalTime() * 1000);
                logger.info("添加定频任务:{},每隔{}秒后执行", taskId, task.getInervalTime());
                break;
            case CUSTOM_CORN_TASK:
                schedule = taskPool.schedule(new CommonTaskExcutor(taskId, task, taskData),
                        new CronTrigger(task.getCronTime()));
                logger.info("添加自定义任务:{},执行时间表达式{}", taskId, task.getCronTime());
                break;
        }

        task.setTaskStatus(TaskStatus.WAITING);
        task.setSchedule(schedule);
    }

    /**
     * 尝试取消任务,当任务执行中,或已执行,无法取消
     *
     * @param taskId
     * @param forced
     */
    @Override
    public void cancelTask(String taskId, boolean forced) throws BusinessException {
        AbstractTask task = this.getTaskById(taskId);
        logger.info("准备取消任务:{}", taskId);
        task.setTaskStatus(TaskStatus.CANCELING);
        ScheduledFuture<?> scheduledFuture = task.getSchedule();
        if (forced && !task.isForcedCancelAble()) {
            throw new BusinessException(BaseExceptionEnums.TASK_CANCEL_UNSUPPORT_ERROR);
        }
        if (scheduledFuture == null) {
            throw new BusinessException(BaseExceptionEnums.TASK_CANCEL_ERROR.setCustomMessage("任务未启动"));
        }
        boolean canceled = scheduledFuture.isCancelled();
        if (!canceled) {
            task.setTaskStatus(TaskStatus.CANCELING);
            //forced 参数正在执行中是否停止,如果设置为强制停止需任务中支持,即处理InterruptedException或处理线程中断状态
            canceled = scheduledFuture.cancel(forced);
        }
        if (canceled) {
            task.setTaskStatus(TaskStatus.CANCELED);
            logger.info("取消任务成功:{}", taskId);
        } else {
            logger.info("取消任务失败:{}", taskId);
            //停止失败处理
            throw new BusinessException(BaseExceptionEnums.TASK_CANCEL_ERROR);
        }
    }


    /**
     * 任务执行器
     */
    private class CommonTaskExcutor implements Runnable {
        private String taskId;
        private AbstractTask task;
        private Object taskData;


        CommonTaskExcutor(String taskId, AbstractTask abstractTask, Object taskData) {
            this.taskId = taskId;
            this.task = abstractTask;
            this.taskData = taskData;
        }

        @Override
        public void run() {
            //执行任务
            task.setTaskStatus(TaskStatus.RUNNING);
            long startTime = System.currentTimeMillis();
            logger.info("任务执行开始:{}", taskId);

            task.runTask(taskData);

            if (task.getTaskType() == TaskType.ONCE_DELAY_TASK) {
                //任务停止
                task.setTaskStatus(TaskStatus.CANCELED);
            }
            task.setTaskStatus(TaskStatus.WAITING);
            logger.info("任务执行完成:{},耗时（ms）:{}", taskId, System.currentTimeMillis() - startTime);
        }
    }
}

