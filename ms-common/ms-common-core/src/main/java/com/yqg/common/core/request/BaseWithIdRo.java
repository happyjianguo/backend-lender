package com.yqg.common.core.request;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 请求对象基类只传递id
 * Created by gao on 2018/6/18.
 */
@Data
public class BaseWithIdRo extends BaseSessionIdRo {

    @ApiModelProperty(value = "id")
    @ReqStringNotEmpty
    private String id;

}
