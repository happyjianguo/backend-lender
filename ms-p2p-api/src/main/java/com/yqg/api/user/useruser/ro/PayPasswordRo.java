package com.yqg.api.user.useruser.ro;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author  zhaoruifeng
 */
@Data
public class PayPasswordRo extends BaseSessionIdRo {
    @ApiModelProperty(value = "短信验证码")
    @JsonProperty
    private String  smsCode;
    @ApiModelProperty(value = "身份证号")
    @JsonProperty
    private String iDcardNo;
    @ApiModelProperty(value = "交易密码")
    @JsonProperty
    private String payPwd;

}
