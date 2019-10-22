package com.yqg.api.user.useruser.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Administrator on 2018/9/5.
 */
@Data
public class UserReq {
    @ApiModelProperty(value = "手机号", required = true)
    private String mobileNumber;
    @ApiModelProperty(value = "身份证号", required = true)
    private String idCardNo;
//    private String userId;
    private String userUuid;
    @ApiModelProperty(value = "操作码", required = true)
    private String opcode;

    @ApiModelProperty(value = "机构名称", required = true)
    private String companyName;

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

    @ApiModelProperty(value = "真实姓名", required = true)
    private String realName;
    @ApiModelProperty(value = "是否支持代扣", required = true)
    private String withholding;
}
