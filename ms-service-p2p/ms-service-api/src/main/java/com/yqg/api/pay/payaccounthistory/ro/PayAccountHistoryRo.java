package com.yqg.api.pay.payaccounthistory.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 资金流水表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 17:28:20
 */
@Data
public class PayAccountHistoryRo {
//订单号
@ApiModelProperty(value = "订单号", required = true)
private String orderNo;
//交易流水号
@ApiModelProperty(value = "交易流水号", required = true)
private String tradeNo;
//交易金额
@ApiModelProperty(value = "交易金额", required = true)
private BigDecimal amount;
//手续费
@ApiModelProperty(value = "手续费", required = true)
private BigDecimal fee;
//状态 1.成功 2.失败 3.处理中
@ApiModelProperty(value = "状态 1.成功 2.失败 3.处理中", required = true)
private Integer status;
//交易类型 1.购买债权  2.放款 3.前置服务费收入 .4.还款 5.投资回款 6.回款收入
@ApiModelProperty(value = "交易类型 1.购买债权  2.放款 3.前置服务费收入 .4.还款 5.投资回款 6.回款收入", required = true)
private Integer tradeType;
//资金出方id
@ApiModelProperty(value = "资金出方id", required = true)
private String fromUserId;
//资金入方id
@ApiModelProperty(value = "资金入方id", required = true)
private String toUserId;
}

