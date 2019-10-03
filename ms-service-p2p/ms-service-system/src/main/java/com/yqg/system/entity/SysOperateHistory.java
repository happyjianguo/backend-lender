package com.yqg.system.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 系统用户表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-09-20 15:52:10
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sysOperateHistory" )
public class SysOperateHistory extends BaseEntity implements Serializable {
    private static final long serialVersionUID=1L;

    //修改字段记录描述
    private String operateString;
    public static final String operateString_field="operateString";

    //操作人名
    private String userName;
    public static final String userName_field="userName";

    //操作类型
    private Integer type;
    public static final String type_field="type";

    //用户操作时的ip
    private String ipAddress;
    public static final String ipAddress_field="ipAddress";


}
