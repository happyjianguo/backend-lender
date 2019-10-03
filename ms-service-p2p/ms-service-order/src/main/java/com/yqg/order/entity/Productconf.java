package com.yqg.order.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 投资产品表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-14 14:45:36
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "productConf" )
public class Productconf extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//借款期限(月)
    private Integer borrowingTerm;
    public static final String borrowingTerm_field="borrowingTerm";
//产品名称
    private String productName;
    public static final String productName_field="productName";
//年化收益率
    private BigDecimal interestRate;
    public static final String interestRate_field="interestRate";
//起投金额
    private BigDecimal lowAmount;
    public static final String lowAmount_field="lowAmount";
//最达投资金额
    private BigDecimal maxAmount;
    public static final String maxAmount_field="maxAmount";
}
