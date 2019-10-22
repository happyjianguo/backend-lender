package com.yqg.api.order.scatterstandard.ro;

import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 散标表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@Data
public class ScatterstandardRo extends BaseSessionIdRo {
    //债权编号
    @ApiModelProperty(value = "债权编号", required = true)
    private String creditorNo;
    //锁定金额
    @ApiModelProperty(value = "锁定金额", required = true)
    private BigDecimal amountLock;
    //购买金额
    @ApiModelProperty(value = "购买金额", required = true)
    private BigDecimal amountBuy;
    //利息
    @ApiModelProperty(value = "利息", required = true)
    private BigDecimal interest;
    //逾期服务费
    @ApiModelProperty(value = "逾期服务费", required = true)
    private BigDecimal overdueFee;
    //逾期滞纳金
    @ApiModelProperty(value = "逾期滞纳金", required = true)
    private BigDecimal overdueRate;
    ////1.投标中 2锁定中 3.满标  4.放款中 5.放款失败 6.放款成功(待还款)  7.还款处理中 8.已还款 9.还款清分处理中  10.还款清分处理成功 11.还款清分处理失败 12.流标
    @ApiModelProperty(value = "订单状态---------1.投标中 2锁定中 3.满标  4.放款中 5.放款失败 6.放款成功(待还款)  7.还款处理中 8.已还款 9.还款清分处理中  10.还款清分处理成功 11.还款清分处理失败 12.流标", required = true)
    private Integer status;
    //理财年化利率
    @ApiModelProperty(value = "理财年化利率", required = true)
    private BigDecimal yearRateFin;
    //放款时间
    @ApiModelProperty(value = "放款时间", required = true)
    private Date lendingTime;
    //应还款时间
    @ApiModelProperty(value = "应还款时间", required = true)
    private Date refundIngTime;
    //实际还款时间
    @ApiModelProperty(value = "实际还款时间", required = true)
    private Date refundActualTime;
    //债权总金额
    @ApiModelProperty(value = "债权总金额", required = true)
    private BigDecimal amountApply;
    //期限
    @ApiModelProperty(value = "期限", required = true)
    private Integer term;

    //还款类型 1正常还款 2分期还款 3展期还款
    @ApiModelProperty(value = "还款类型", required = true)
    private Integer repaymentType = 1;
    //分期期数
    @ApiModelProperty(value = "分期期数 ", required = true)
    private Integer  periodNo;
}

