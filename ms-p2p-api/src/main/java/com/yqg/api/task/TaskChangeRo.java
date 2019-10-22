package com.yqg.api.task;

import com.yqg.common.core.annocation.ReqIntGreaterThan0;
import com.yqg.common.core.annocation.ReqStringNotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 延迟任务
 * Created by gao on 2018/7/20.
 */
@Data
public class TaskChangeRo extends TaskStopRo  {

    @ReqStringNotEmpty
    @ApiModelProperty(value = "任务类型", required = true)
    private String taskType;

    @ApiModelProperty(value = "修改后是否直接启动,默认启动")
    private boolean startAfterChange=true;

    @ReqIntGreaterThan0
    @ApiModelProperty(value = "延迟时间,单位秒", required = true)
    private int delayTime;

    @ReqIntGreaterThan0
    @ApiModelProperty(value = "间隔时间,单位秒", required = true)
    private int inervalTime;

    @ApiModelProperty(value = "任务执行时间表达式", required = true)
    String corn;

}
