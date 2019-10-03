package com.yqg.user.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户账户明细表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-11 10:43:36
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "userAccountHistory" )
public class UserAccounthistory extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//用户id
    private String userUuid;
    public static final String userUuid_field="userUuid";
//交易金额
    private BigDecimal tradeBalance;
    public static final String tradeBalance_field="tradeBalance";
//可用余额
    private BigDecimal availableBalance;
    public static final String availableBalance_field="availableBalance";
//冻结余额
    private BigDecimal lockAmount;
    public static final String lockAmount_field="lockAmount";
//交易信息 
    private String tradeInfo;
    public static final String tradeInfo_field="tradeInfo";
//交易类型 1.购买债权  2.回款
    private Integer type;
    public static final String type_field="type";
//产品类型 1.散标 2活期账户 3.理财账户
    private Integer productType;
    public static final String productType_field="productType";
}
