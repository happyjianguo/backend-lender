package com.yqg.system.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sysPaymentChannel" )
public class SysPaymentChannel extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//支付通道编码
    private String paymentChannelCode;
    public static final String paymentChannelCode_field="paymentChannelCode";
//支付通道名称
    private String paymentChannelName;
    public static final String paymentChannelName_field="paymentChannelName";
//通道类型
    private Integer type;
    public static final String type_field="type";
//是否有效(0=无效，1=有效)
    private Integer status;
    public static final String status_field="status";
}
