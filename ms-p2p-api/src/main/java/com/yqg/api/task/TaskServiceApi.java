package com.yqg.api.task;

/**
 * 任务服务接口
 * Created by gao on 2018/7/6.
 */
public class TaskServiceApi {

    public static final String serviceName = "service-task";

    /**
     * 任务列表
     */
    public static final String path_taskList = "/task/taskList";

    /**
     * 启动任务
     */
    public static final String path_startTask = "/task/startTask";
    /**
     * 停止任务
     */
    public static final String path_cancelTask = "/task/cancelTask";
    /**
     * 更改任务执行时间
     */
    public static final String path_changeTask = "/task/changeTask";


}
