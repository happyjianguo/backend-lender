package com.yqg.api.order.orderorder.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Lixiangjun on 2019/7/1.
 */
@Data
public class RepaymentPlanBo {
    private String id;

    //债权编号
    private String creditorNo;
    //放款时间
    private Date lendingTime;
    //'应还款时间'
    private Date refundIngTime;
    //'实际还款时间'
    private Date refundActualTime;
    //应还款金额
    private BigDecimal refundIngAmount;
    //实际还款金额
    private BigDecimal amountActual;
    //还款状态(1.待还款 2.已还款 3,逾期已还款)
    private Integer status;
    //'期数'
    private Integer periodNo;
}
