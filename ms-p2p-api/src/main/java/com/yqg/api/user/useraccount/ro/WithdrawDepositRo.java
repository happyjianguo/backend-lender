package com.yqg.api.user.useraccount.ro;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yqg.common.core.request.BasePageRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: hyy
 * @Date: 2019/5/21 11:17
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@Data
public class WithdrawDepositRo extends BaseSessionIdRo {
    @JsonProperty
    @ApiModelProperty(value = "用户id", required = true)
    private String userId;
    @JsonProperty
    @ApiModelProperty(value = "提现金额", required = true)
    private BigDecimal amount;
    @JsonProperty
    @ApiModelProperty(value = "银行卡号", required = true)
    private String bankNumberNo;
    @JsonProperty
    @ApiModelProperty(value = "交易密码", required = true)
    private String passWord;
}