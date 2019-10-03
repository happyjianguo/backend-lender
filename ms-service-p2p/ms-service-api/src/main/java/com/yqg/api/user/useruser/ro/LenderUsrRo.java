package com.yqg.api.user.useruser.ro;

import com.yqg.common.core.BaseBo;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Administrator on 2018/9/4.
 */
@Data
public class LenderUsrRo {
    @ApiModelProperty(value = "手机号", required = true)
    private String mobileNumber;
    @ApiModelProperty(value = "身份证号", required = true)
    private String idCardNo;
}
