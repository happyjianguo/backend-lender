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
 * 债权人的基本信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 10:40:02
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "orderOrder" )
public class OrderOrder extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//债权编号
//    private String creditorNo;
//    public static final String creditorNo_field="creditorNo";
//理财用户ID
    private String userUuid;
    public static final String userUuid_field="userUuid";
//购买金额
    private BigDecimal applyBuy;
    public static final String applyBuy_field="applyBuy";
    private BigDecimal amountBuy;
    public static final String amountBuy_field="amountBuy";
    //购买金额
    private BigDecimal chargeBuy;
    public static final String chargeBuy_field="chargeBuy";
////期限
//    private Integer term;
//    public static final String term_field="term";
////借款年化利率
//    private BigDecimal yearRate;
//    public static final String yearRate_field="yearRate";
////理财年化利率
//    private BigDecimal yearRateFin;
//    public static final String yearRateFin_field="yearRateFin";
//购买时间
    private Date buyTime;
    public static final String buyTime_field="buyTime";
//付款时间
    private Date incomeTime;
    public static final String incomeTime_field="incomeTime";
////到期时间
//    private Date dueTime;
//    public static final String dueTime_field="dueTime";
////实际回款时间
//    private Date actReturnTime;
//    public static final String actReturnTime_field="actReturnTime";
//收益
//    private BigDecimal income;
//    public static final String income_field="income";
//(1.投资处理中 2.投资失败 3,投资成功  4.过期 5.回款成功 6.回款失败 7.回款处理中 )
    private Integer status;
    public static final String status_field="status";
//产品类型--1.散标 2活期账户 3.理财账户
    private Integer productType;
    public static final String productType_field="productType";
//    private BigDecimal canpay;
//    public static final String canpay_field="canpay";
//    private BigDecimal actRepayAmount;
//    public static final String actRepayAmount_field="actRepayAmount";
//    //0理财首投1理财复投2债转
//    private Integer type;

}
