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
 * 订单散标关系表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-27 15:04:24
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "orderScatterStandardRel" )
public class OrderScatterStandardRel extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//订单号
    private String orderNo;
    public static final String orderNo_field="orderNo";

    private String creditorNo;
    public static final String code_field="creditorNo";
//购买金额
    private BigDecimal amount;
    public static final String amount_field="amount";
//投资人ID
    private String buyUser;
    public static final String buyUser_field="buyUser";
//投资人类型 0普通投资人1超级投资人
    private Integer userType;
    public static final String userType_field="userType";

}
