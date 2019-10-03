package com.yqg.pay.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 资金流水表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 17:28:20
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "payAccountHistory" )
public class PayAccountHistory extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//订单号
    private String orderNo;
    public static final String orderNo_field="orderNo";
//交易流水号
    private String tradeNo;
    public static final String tradeNo_field="tradeNo";
//交易金额
    private BigDecimal amount;
    public static final String amount_field="amount";
//手续费
    private BigDecimal fee;
    public static final String fee_field="fee";
//状态 1.成功 2.失败 3.处理中
    private Integer status;
    public static final String status_field="status";
//交易类型 1.购买债权  2.放款 3.前置服务费收入 .4.还款 5.投资回款 6.回款收入
    private Integer tradeType;
    public static final String tradeType_field="tradeType";
//资金出方id
    private String fromUserId;
    public static final String fromUserId_field="fromUserId";
//资金入方id
    private String toUserId;
    public static final String toUserId_field="toUserId";
}
