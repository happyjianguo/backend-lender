package com.yqg.api.user.useraccount.ro;

import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户账户表 请求对象
 *
 * @author wu
 * @email wsc@yishufu.com
 * @date 2018-08-31 14:16:46
 */
@Data
public class UseraccountRo extends BaseSessionIdRo {

    //活期账户余额
    @ApiModelProperty(value = "当前可用账户余额", required = true)
    private BigDecimal currentBalance;
    //活期账户冻结金额
    @ApiModelProperty(value = "锁定账户余额", required = true)
    private BigDecimal lockedBalance;
    //定期账户余额
    @ApiModelProperty(value = "在投金额", required = true)
    private BigDecimal investingBanlance;
    //金额
    @ApiModelProperty(value = "金额", required = true)
    private BigDecimal amount;
    //用户类型
    @ApiModelProperty(value = "用户类型", required = true)
    private Integer type;
}

