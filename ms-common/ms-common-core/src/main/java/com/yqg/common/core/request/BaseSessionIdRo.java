package com.yqg.common.core.request;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 请求对象基类
 * Created by gao on 2018/6/18.
 */
@Data
public class BaseSessionIdRo extends BaseRo{

    @ApiModelProperty(value = "会话id")
    @ReqStringNotEmpty
    private String sessionId;

    /**
     * 由系统通过sessionId转换,无需传递
     */
    @ApiModelProperty(value = "用户id",hidden = true)
    private String userId;
}
