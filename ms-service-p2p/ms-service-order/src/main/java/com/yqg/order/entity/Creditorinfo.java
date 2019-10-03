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
 * 债权表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "creditorInfo" )
public class Creditorinfo extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//借款用途
    private String borrowingPurposes;
    public static final String borrowingPurposes_field="borrowingPurposes";
//风险等级
    private Integer riskLevel;
    public static final String riskLevel_field="riskLevel";
//债权编号
    private String creditorNo;
    public static final String creditorNo_field="creditorNo";
//借款人用户uuid
    private String lenderId;
    public static final String lenderId_field="lenderId";
//申请金额
    private BigDecimal amountApply;
    public static final String amountApply_field="amountApply";
//期限
    private Integer term;
    public static final String term_field="term";
//借款年化利率
    private BigDecimal borrowerYearRate;
    public static final String borrowerYearRate_field="borrowerYearRate";
//前置服务费
    private BigDecimal serviceFee;
    public static final String serviceFee_field="serviceFee";
//发标时间
    private Date biddingTime;
    public static final String biddingTime_field="biddingTime";
//来源
    private Integer channel;
    public static final String channel_field="channel";
//银行code
    private String bankCode;
    public static final String bankCode_field="bankCode";
//银行名称
    private String bankName;
    public static final String bankName_field="bankName";
//银行卡号
    private String bankNumber;
    public static final String bankNumber_field="bankNumber";
//银行卡持卡人
    private String bankCardholder;
    public static final String bankCardholder_field="bankCardholder";
}
