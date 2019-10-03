package com.yqg.api.orderpackagerel.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单债权包关系表 请求对象
 *
 * @author admin
 * @email admin@yishufu.com
 * @date 2018-11-27 14:57:50
 */
@Data
public class OrderpackagerelRo{
//订单号
@ApiModelProperty(value = "订单号", required = true)
private String orderNo;
//债权包编号
@ApiModelProperty(value = "债权包编号", required = true)
private String code;
//排序自增字段
@ApiModelProperty(value = "排序自增字段", required = true)
private Integer sort;
//购买债券包金额
@ApiModelProperty(value = "购买债券包金额", required = true)
private BigDecimal amount;
//投资人ID
@ApiModelProperty(value = "投资人ID", required = true)
private String buyUser;
//投资人类型 0普通投资人1超级投资人
@ApiModelProperty(value = "投资人类型 0普通投资人1超级投资人", required = true)
private Integer userType;
}

