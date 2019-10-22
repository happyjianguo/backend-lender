package com.yqg.order.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Lixiangjun on 2019/7/1.
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "repaymentPlan" )
public class RepaymentPlan extends BaseEntity implements Serializable {
    private static final long serialVersionUID=1L;

    //债权编号
    private String creditorNo;
    public static final String creditorNo_field="creditorNo";
    //放款时间
    private Date lendingTime;
    public static final String lendingTime_field="lendingTime";
    //'应还款时间'
    private Date refundIngTime;
    public static final String refundIngTime_field="refundIngTime";
    //还款状态(1.待还款，2.还款处理中, 3.已还款，4.逾期已还款 )
    private Integer status;
    public static final String status_field="status";
    //'期数'
    private Integer periodNo;
    public static final String periodNo_field="periodNo";
    //应还款金额
    private BigDecimal refundIngAmount;
    public static final String refundIngAmount_field="refundIngAmount";

    //放款时间
    private Date actualRefundTime;
    public static final String actualRefundTime_field="actualRefundTime";
    private BigDecimal amountActual;
    public static final String amountActual_field="amountActual";
    //还款本金
    private BigDecimal amountRepay;
    public static final String amountRepay_field="amountRepay";

//    private String paymentcode;
//    public static final String paymentcode_field="paymentcode";
//    private String depositStatus;
//    public static final String depositStatus_field="depositStatus";
//    private String depositChannel;
//    public static final String depositChannel_field="depositChannel";
//    private String externalId;
//    public static final String externalId_field="externalId";
//    private String transactionId;
//    public static final String transactionId_field="transactionId";
}
