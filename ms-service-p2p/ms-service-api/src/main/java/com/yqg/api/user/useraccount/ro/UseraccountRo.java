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
////用户id
//@ApiModelProperty(value = "用户id", required = true)
//private String userUuid;
//散标账户在投金额
@ApiModelProperty(value = "散标账户在投金额", required = true)
private BigDecimal sanbiaoBalance;
//散标账户冻结金额
@ApiModelProperty(value = "散标账户冻结金额", required = true)
private BigDecimal sanbiaoLockBalance;
//活期账户余额
@ApiModelProperty(value = "活期账户余额", required = true)
private BigDecimal currentBalance;
//活期账户冻结金额
@ApiModelProperty(value = "活期账户冻结金额", required = true)
private BigDecimal currentLockBalance;
//定期账户余额
@ApiModelProperty(value = "定期账户余额", required = true)
private BigDecimal depositBalance;
//定期账户冻结金额
@ApiModelProperty(value = "定期账户冻结金额", required = true)
private BigDecimal depositLockBalance;
//金额
@ApiModelProperty(value = "金额", required = true)
private BigDecimal amount;
}

