package com.yqg.task.service;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.task.enums.TaskStatus;
import com.yqg.task.enums.TaskType;
import com.yqg.task.config.TaskConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledFuture;

/**
 * 任务抽象类
 * Created by gao on 2017/9/3.
 */
@Component
public abstract class AbstractTask implements ITask {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TaskService taskService;

    private String taskId;
    private TaskType taskType;
    private TaskStatus taskStatus = TaskStatus.CANCELED;
    private int delayTime = 0;
    private int inervalTime = 0;
    private String cronTime = "";
    private boolean defaultStart = false;
    ////forced 参数正在执行中是否停止,如果设置为强制停止需任务中支持,即处理InterruptedException或处理线程中断状态
    private boolean forcedCancelAble=false;
    private ScheduledFuture<?> schedule;

    public boolean isForcedCancelAble() {
        return forcedCancelAble;
    }

    public void setForcedCancelAble(boolean forcedCancelAble) {
        this.forcedCancelAble = forcedCancelAble;
    }

    public ScheduledFuture<?> getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduledFuture<?> schedule) {
        this.schedule = schedule;
    }

    public boolean isDefaultStart() {
        return defaultStart;
    }

    public void setDefaultStart(boolean defaultStart) {
        this.defaultStart = defaultStart;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public void setInervalTime(int inervalTime) {
        this.inervalTime = inervalTime;
    }

    public void setCronTime(String cronTime) {
        this.cronTime = cronTime;
    }

    @Override
    public abstract TaskType getTaskType();

    /**
     * 延迟执行时间（秒）
     *
     * @return
     */
    @Override
    public int getDelayTime() {
        return delayTime;
    }

    /**
     * 间隔执行时间（秒）
     *
     * @return
     */
    @Override
    public int getInervalTime() {
        return inervalTime;
    }

    /**
     * 执行表达式
     *
     * @return
     */
    @Override
    public String getCronTime() {
        return cronTime;
    }

    /**
     * 执行任务
     *
     * @param taskData
     */
    @Override
    public abstract void runTask(Object taskData);

    @PostConstruct
    private void addToTaskMap() {
        String taskId = this.getClass().getName();
        this.setTaskId(taskId);
        TaskConfig.taskMap.put(taskId, this);
        if (this.isDefaultStart()) {
            try {
                taskService.startTask(taskId, null);
                logger.info("任务{}自动启动成功", taskId);
            } catch (BusinessException ex) {
                logger.error("任务{}自动启动失败,{}", taskId,ex.getExceptionEnum().getMessage());
            }
        }
    }
}
