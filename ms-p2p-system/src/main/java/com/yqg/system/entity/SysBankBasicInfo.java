package com.yqg.system.entity;

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
 * 银行基础信息
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sysBankBasicInfo" )
public class SysBankBasicInfo extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//银行名称
    private String bankName;
    public static final String bankName_field="bankName";
//银行简称
    private String bankCode;
    public static final String bankCode_field="bankCode";
//是否可用(1=是，0=否)
    private Integer isUsed;
    public static final String isUsed_field="isUsed";
//维护开始时间
    private Date protectStartTime;
    public static final String protectStartTime_field="protectStartTime";
//维护结束时间
    private Date protectEndTime;
    public static final String protectEndTime_field="protectEndTime";
//单笔限额
    private BigDecimal singleLimit;
    public static final String singleLimit_field="singleLimit";
//单日限额
    private BigDecimal oneDayLimit;
    public static final String oneDayLimit_field="oneDayLimit";
}
