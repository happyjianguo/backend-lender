package com.yqg.api.user.useruser.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserAuthBankStatusRo {
    @ReqStringNotEmpty
    @ApiModelProperty(value = "用户id", required = true)
    String userId;
}
