package com.yqg.common.core.request;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import io.swagger.annotations.ApiModelProperty;

/**
 * 加签请求
 * Created by gao on 2018/7/7.
 */
public class BaseSignRo extends BaseSessionIdRo {

    @ApiModelProperty(value = "签名",required = true)
    @ReqStringNotEmpty
    private String sign;

}
