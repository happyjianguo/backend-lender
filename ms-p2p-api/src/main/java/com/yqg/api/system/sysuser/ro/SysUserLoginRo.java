package com.yqg.api.system.sysuser.ro;


import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysUserLoginRo extends BaseSessionIdRo {
    @ReqStringNotEmpty
    @ApiModelProperty(value = "用户名", required = true)
    String username;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "密码", required = true)
    String password;
}
