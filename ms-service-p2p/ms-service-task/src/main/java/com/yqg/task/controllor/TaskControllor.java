package com.yqg.task.controllor;

import com.yqg.api.task.TaskChangeRo;
import com.yqg.api.task.TaskRo;
import com.yqg.api.task.TaskServiceApi;
import com.yqg.api.task.TaskStopRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.task.enums.TaskType;
import com.yqg.task.service.AbstractTask;
import com.yqg.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * 启动、停止、重置时间
 * Created by gao on 2018/7/11.
 */
@RestController
public class TaskControllor extends BaseControllor {
    @Autowired
    TaskService taskService;

    @NotNeedLogin
    @PostMapping(value = TaskServiceApi.path_taskList)
    public BaseResponse<Collection<AbstractTask>> taskList() throws Exception {
        return new BaseResponse<Collection<AbstractTask>>().successResponse(taskService.taskList());
    }

    @NotNeedLogin
    @PostMapping(value = TaskServiceApi.path_startTask)
    public BaseResponse<AbstractTask> startTask(@RequestBody TaskRo taskRo) throws Exception {

        taskService.startTask(taskRo.getTaskId(), taskRo.getTaskData());
        return new BaseResponse<AbstractTask>().successResponse(taskService.getTaskById(taskRo.getTaskId()));
    }

    @NotNeedLogin
    @PostMapping(value = TaskServiceApi.path_cancelTask)
    public BaseResponse<AbstractTask> cancelTask(@RequestBody TaskStopRo taskStopRo) throws Exception {
        taskService.cancelTask(taskStopRo.getTaskId(), taskStopRo.isForced());
        return new BaseResponse<AbstractTask>().successResponse(taskService.getTaskById(taskStopRo.getTaskId()));
    }

    @NotNeedLogin
    @PostMapping(value = TaskServiceApi.path_changeTask)
    public BaseResponse<AbstractTask> changeTask(@RequestBody TaskChangeRo taskChangeRo) throws Exception {
        String taskId = taskChangeRo.getTaskId();
        TaskType taskType = TaskType.valueOf(taskChangeRo.getTaskType());

        AbstractTask task = taskService.getTaskById(taskId);
        switch (taskType) {
            case ONCE_DELAY_TASK:
                int delayTime = taskChangeRo.getDelayTime();
                if (delayTime <= 0) {
                    throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
                }
                task.setDelayTime(delayTime);
                break;
            case FIX_RATE_TASK:
                int inervalTime = taskChangeRo.getInervalTime();
                if (inervalTime <= 0) {
                    throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
                }
                task.setInervalTime(inervalTime);
                break;
            case CUSTOM_CORN_TASK:
                String corn = taskChangeRo.getCorn();
                if (StringUtils.isEmpty(corn)) {
                    throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
                }
                task.setCronTime(corn);
                break;
        }
        taskService.cancelTask(taskId, taskChangeRo.isForced());
        if (taskChangeRo.isStartAfterChange()) {
            taskService.startTask(taskId, taskChangeRo.getTaskData());
        }
        return  new BaseResponse<AbstractTask>().successResponse(taskService.getTaskById(taskId));
    }

}
