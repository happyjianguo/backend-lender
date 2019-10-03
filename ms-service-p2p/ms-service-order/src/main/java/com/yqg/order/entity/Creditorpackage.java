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
 * 债权包表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-15 18:21:38
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "creditorPackage")
public class Creditorpackage extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //产品uuid
    private String productUuid;
    public static final String productUuid_field = "productUuid";
    //债权编号
    private String code;
    public static final String code_field = "code";
    //状态
    private Integer status;
    public static final String status_field = "status";
    private Date endTime;
    public static final String endTime_field = "endTime";
    private BigDecimal amount;
    public static final String amount_field = "amount";
    private BigDecimal buyAmount;
    public static final String buyAmount_field = "buyAmount";
}
