package com.yqg.common.core.request;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础请求对象
 * Created by gao on 2018/7/7.
 */
@Data
public class BaseRo {

    @ApiModelProperty(value = "客户端类型")
    private String clientType;

    @ApiModelProperty(value = "ip地址")
    private String clientIp;

    @ApiModelProperty(value = "语言")
    private String language;

}
