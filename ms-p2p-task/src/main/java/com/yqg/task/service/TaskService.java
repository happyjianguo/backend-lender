package com.yqg.task.service;

import com.yqg.common.exceptions.BusinessException;

import java.util.*;

/**
 * 任务服务接口
 * Created by gaohaiming on 2017/8/10.
 */
public interface TaskService {


    /**
     * 任务列表
     *
     * @return
     */
    Collection<AbstractTask> taskList();

    /**
     * 查询任务
     *
     * @param taskId
     * @return
     */
    AbstractTask getTaskById(String taskId) throws BusinessException;

    /**
     * 添加任务
     *
     * @param taskId
     */
    void startTask(String taskId, Object taskData) throws BusinessException;

    /**
     * 尝试取消任务,当任务执行中,或已执行,无法取消
     *
     * @param taskId
     */
    void cancelTask(String taskId, boolean forced) throws BusinessException;


}

