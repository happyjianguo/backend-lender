package com.yqg.system.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户角色中间表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sysUserRole" )
public class SysUserRole extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//用户ID
    private String userId;
    public static final String userId_field="userId";
//角色ID
    private String roleId;
    public static final String roleId_field="roleId";
//状态,0=有效，1=无效
    private Integer status;
    public static final String status_field="status";

    /*@ManyToOne
    private SysUser sysUser;*/
}
