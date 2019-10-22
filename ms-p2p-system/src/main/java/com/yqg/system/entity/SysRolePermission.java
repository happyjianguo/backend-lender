package com.yqg.system.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 系统角色权限表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sysRolePermission" )
public class SysRolePermission extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//角色ID
    private String roleId;
    public static final String roleId_field="roleId";
//权限ID
    private String permissionId;
    public static final String permissionId_field="permissionId";
}
