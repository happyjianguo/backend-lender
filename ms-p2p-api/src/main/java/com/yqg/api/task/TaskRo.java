package com.yqg.api.task;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by gao on 2018/7/20.
 */
@Data
public class TaskRo {

    @ReqStringNotEmpty
    @ApiModelProperty(value = "任务编号", required = true)
    String taskId;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "任务数据")
    Object taskData;
}
