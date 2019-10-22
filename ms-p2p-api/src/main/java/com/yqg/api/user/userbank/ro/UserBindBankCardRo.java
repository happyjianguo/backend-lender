package com.yqg.api.user.userbank.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserBindBankCardRo extends BaseSessionIdRo {
    @ReqStringNotEmpty
    @ApiModelProperty(value = "持卡人姓名", required = true)
    private String bankHolderName;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "银行卡code", required = true)
    private String bankCode;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "银行卡号", required = true)
    private String bankNumberNo;
    @ReqStringNotEmpty
    @ApiModelProperty(value = "交易密码", required = true)
    private String payPwd;
}
