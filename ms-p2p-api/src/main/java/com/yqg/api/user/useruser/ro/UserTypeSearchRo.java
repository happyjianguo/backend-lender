package com.yqg.api.user.useruser.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserTypeSearchRo {
    @ReqStringNotEmpty
    @ApiModelProperty(value = "用户类型", required = true)
    private Integer userType;
    @ApiModelProperty(value = "银行类型")
    private String bankType;
}
