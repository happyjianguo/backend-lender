package com.yqg.api.user.useruser.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserRegistRo extends BaseSessionIdRo {
    @ReqStringNotEmpty
    @ApiModelProperty(value = "手机号", required = true)
    String mobileNumber;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "短信验证码", required = true)
    String smsCode;

    @ApiModelProperty(value = "用户类型", required = false)
    Integer type;
}
