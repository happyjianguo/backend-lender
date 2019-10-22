package com.yqg.system.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 系统角色表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sysRole" )
public class SysRole extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//角色名称
    private String roleName;
    public static final String roleName_field="roleName";
//状态,0=有效，1=无效
    private Integer status;
    public static final String status_field="status";
}
