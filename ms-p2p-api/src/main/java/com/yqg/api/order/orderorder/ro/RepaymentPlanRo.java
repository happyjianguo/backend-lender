package com.yqg.api.order.orderorder.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Lixiangjun on 2019/7/1.
 */
@Data
public class RepaymentPlanRo {

    @ApiModelProperty(value = "债权编号", required = true)
    private String creditorNo;
    //还款状态(1.待还款 2.已还款 3,逾期已还款)
    @ApiModelProperty(value = "还款状态", required = true)
    private Integer status;
    @ApiModelProperty(value = "期数", required = true)
    private Integer periodNo;

    //放款时间
    private Date lendingTime;
    //'应还款时间'
    private Date refundIngTime;
    //应还款金额
    private BigDecimal refundIngAmount;
    //签名
    @ApiModelProperty(value = "签名", required = true)
    private String sign;


    private List<RepaymentPlanRo> list;
}
