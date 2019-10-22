package com.yqg.system.entity;


import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * @author niebiaofei
 * 
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sysSmsMessageInfo" )
public class SysSmsMessageInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 636884001018202591L;

    private Integer smsType;
    public static final String smsType_field="smsType";
    private String smsConent;
    public static final String smsConent_field="smsConent";
    private String smsCode;
    public static final String smsCode_field="smsCode";
    private String mobile;
    public static final String mobile_field="mobile";
    private String batchId;
    public static final String batchId_field="batchId";

}