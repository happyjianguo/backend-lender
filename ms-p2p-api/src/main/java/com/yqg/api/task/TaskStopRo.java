package com.yqg.api.task;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务停止请求
 * Created by gao on 2018/7/20.
 */
@Data
public class TaskStopRo extends TaskRo{

    @ApiModelProperty(value = "任务执行中是否强制停止,需任务本身支持,默认false")
    boolean forced=false;
}
