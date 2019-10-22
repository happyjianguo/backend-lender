package com.yqg.task.enums;

/**
 * 任务状态
 * Created by gao on 2018/7/17.
 */
public enum TaskStatus {
    /**
     * 启动中
     */
    STARTING,
    /**
     * 待执行
     */
    WAITING,
    /**
     * 运行中
     */
    RUNNING,
    /**
     * 取消中
     */
    CANCELING,
    /**
     * 已取消
     */
    CANCELED
}
