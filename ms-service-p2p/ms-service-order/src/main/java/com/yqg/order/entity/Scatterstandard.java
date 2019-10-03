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
 * 散标表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "scatterStandard" )
public class Scatterstandard extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//债权编号
    private String creditorNo;
    public static final String creditorNo_field="creditorNo";
//锁定金额
    private BigDecimal amountLock;
    public static final String amountLock_field="amountLock";
//购买金额
    private BigDecimal amountBuy;
    public static final String amountBuy_field="amountBuy";
//利息
    private BigDecimal interest;
    public static final String interest_field="interest";
//逾期服务费
    private BigDecimal overdueFee;
    public static final String overdueFee_field="overdueFee";
//逾期滞纳金
    private BigDecimal overdueRate;
    public static final String overdueRate_field="overdueRate";
    private Integer status;
    public static final String status_field="status";
//理财年化利率
    private BigDecimal yearRateFin;
    public static final String yearRateFin_field="yearRateFin";
//放款时间
    private Date lendingTime;
    public static final String lendingTime_field="lendingTime";
//应还款时间
    private Date refundIngTime;
    public static final String refundIngTime_field="refundIngTime";
//实际还款时间
    private Date refundActualTime;
    public static final String refundActualTime_field="refundActualTime";
    //债权总金额
    private BigDecimal amountApply;
    public static final String amountApply_field="amountApply";

    //期限
    private Integer term;
    public static final String term_field="term";

    //借款用途
    private String borrowingPurposes;
    public static final String borrowingPurposes_field="borrowingPurposes";

    private String paymentcode;
    public static final String paymentcode_field="paymentcode";


    private BigDecimal amountActual;
    public static final String amountActual_field="amountActual";

    private String depositStatus;
    public static final String depositStatus_field="depositStatus";

    private String externalId;
    public static final String externalId_field="externalId";

    private String transactionId;
    public static final String transactionId_field="transactionId";
    private String depositMethod;
    public static final String depositMethod_field="depositMethod";
    private String depositChannel;
    public static final String depositChannel_field="depositChannel";
    private String packageNo;
    public static final String packageNo_field="packageNo";
}
