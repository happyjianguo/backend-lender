package com.yqg.task.service;

import com.yqg.task.enums.TaskType;

/**
 * 任务接口
 * Created by gao on 2018/7/17.
 */
public interface ITask {

    /**
     * 任务类型
     *
     * @return
     */
    TaskType getTaskType();

    /**
     * 延迟执行时间（秒）
     *
     * @return
     */
    int getDelayTime();

    /**
     * 间隔执行时间（秒）
     *
     * @return
     */
    int getInervalTime();

    /**
     * 执行表达式
     *
     * @return
     */
    String getCronTime();

    /**
     * 执行任务
     *
     * @param taskData
     */
    void runTask(Object taskData);
}
