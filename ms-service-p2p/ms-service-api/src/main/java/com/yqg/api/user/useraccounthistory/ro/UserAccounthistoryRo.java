package com.yqg.api.user.useraccounthistory.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户账户明细表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-11 10:43:36
 */
@Data
public class UserAccounthistoryRo {
//用户id
@ApiModelProperty(value = "用户id", required = true)
private String userUuid;
//交易金额
@ApiModelProperty(value = "交易金额", required = true)
private BigDecimal tradeBalance;
//可用余额
@ApiModelProperty(value = "可用余额", required = true)
private BigDecimal availableBalance;
//冻结余额
@ApiModelProperty(value = "冻结余额", required = true)
private BigDecimal lockAmount;
//交易信息 
@ApiModelProperty(value = "交易信息 ", required = true)
private String tradeInfo;
//交易类型 1.充值  2.购买债权 3.释放债权 4.回款
@ApiModelProperty(value = "交易类型 1.充值  2.购买债权 3.释放债权 4.回款", required = true)
private Integer type;
//产品类型 1.散标 2活期账户 3.定期账户
@ApiModelProperty(value = "产品类型 1.散标 2活期账户 3.定期账户", required = true)
private Integer productType;
}

