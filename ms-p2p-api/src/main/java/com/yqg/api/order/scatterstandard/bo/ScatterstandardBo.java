package com.yqg.api.order.scatterstandard.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 散标表 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@Data
public class ScatterstandardBo {
    private String creditorNo;
    private BigDecimal amountLock;
    private BigDecimal amountBuy;
    private BigDecimal interest;
    private BigDecimal overdueFee;
    private BigDecimal overdueRate;
    private Integer status;
    private String yearRateFin;
    private Date lendingTime;
    private Date refundIngTime;
    private Date refundActualTime;
    private BigDecimal amountApply;
    private String term;
    private String borrowingPurposes;
    private String paymentcode;
    private BigDecimal amountActual;
    private String depositStatus;//还款状态
    private String externalId;
    private String transactionId;
    private String depositMethod;//还款方式
    private String depositChannel;//还款渠道
    private String packageNo;//包
    private Date createTime;

}

