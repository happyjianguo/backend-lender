package com.yqg.api.system.sms.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Lixiangjun on 2019/6/10.
 */
@Data
public class UserSmsCodeRo extends BaseSessionIdRo {

    @ReqStringNotEmpty
    @ApiModelProperty(value = "手机号", required = true)
    String mobileNumber;
}
