package com.yqg.api.system.sysuser.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysUserChangePasswdRo extends BaseSessionIdRo {
    @ApiModelProperty(value = "用户id", required = true)
    String uuid;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "用户老密码", required = true)
    String oldPassword;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "用户新密码", required = true)
    String newPassword;
}
