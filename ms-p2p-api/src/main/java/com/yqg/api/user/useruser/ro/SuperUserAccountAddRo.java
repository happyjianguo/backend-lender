package com.yqg.api.user.useruser.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SuperUserAccountAddRo extends BaseSessionIdRo {
    @ReqStringNotEmpty
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "被添加用户", required = true)
    private String userUuid;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "添加金额", required = true)
    private String amount;

}
