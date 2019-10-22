package com.yqg.api.order.orderorder.ro;

import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 债权人的基本信息表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 10:40:02
 */
@Data
public class OrderOrderRo extends BaseSessionIdRo{
@ApiModelProperty(value = "订单编号", required = true)
private String orderNo;
@ApiModelProperty(value = "userUuid", required = true)
private String userUuid;
////购买金额
//@ApiModelProperty(value = "购买金额", required = true)
//private BigDecimal amountBuy;
////期限
//@ApiModelProperty(value = "期限", required = true)
//private Integer term;
////借款年化利率
//@ApiModelProperty(value = "借款年化利率", required = true)
//private BigDecimal yearRate;
////理财年化利率
//@ApiModelProperty(value = "理财年化利率", required = true)
//private BigDecimal yearRateFin;
////前置服务费
//@ApiModelProperty(value = "前置服务费", required = true)
//private BigDecimal serviceFee;
////购买时间
//@ApiModelProperty(value = "购买时间", required = true)
//private Date buyTime;
////到期时间
//@ApiModelProperty(value = "到期时间", required = true)
//private Date dueTime;
////实际回款时间
//@ApiModelProperty(value = "实际回款时间", required = true)
//private Date actReturnTime;
////收益
//@ApiModelProperty(value = "收益", required = true)
//private BigDecimal income;
////(1.投资处理中 2.投资失败 3,投资成功  4.过期 5.回款成功 6.回款失败 7.回款处理中 )
//@ApiModelProperty(value = "(1.投资处理中 2.投资失败 3,投资成功  4.过期 5.回款成功 6.回款失败 7.回款处理中 )", required = true)
//private Integer status;
////产品类型--1.散标 2活期账户 3.理财账户
//@ApiModelProperty(value = "产品类型--1.散标 2活期账户 3.理财账户", required = true)
//private Integer productType;
}

