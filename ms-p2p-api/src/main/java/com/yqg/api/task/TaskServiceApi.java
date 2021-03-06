package com.yqg.api.task;

/**
 * 任务服务接口
 * Created by gao on 2018/7/6.
 */
public class TaskServiceApi {

    public static final String serviceName = "service-task";

    public static final String path_healthcheck = "/api-task/healthcheck";
    /**
     * 任务列表
     */
    public static final String path_taskList = "/api-task/task/taskList";

    /**
     * 启动任务
     */
    public static final String path_startTask = "/api-task/task/startTask";
    /**
     * 停止任务
     */
    public static final String path_cancelTask = "/api-task/task/cancelTask";
    /**
     * 更改任务执行时间
     */
    public static final String path_changeTask = "/api-task/task/changeTask";


}
