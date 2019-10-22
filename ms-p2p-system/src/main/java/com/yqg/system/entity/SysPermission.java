package com.yqg.system.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 系统权限表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "SysPermission" )
public class SysPermission extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//父权限ID
    private String parentId;
    public static final String parentId_field="parentId";
//权限名称
    private String permissionName;
    public static final String permissionName_field="permissionName";
//权限码
    private String permissionCode;
    public static final String permissionCode_field="permissionCode";
//权限路径
    private String permissionUrl;
    public static final String permissionUrl_field="permissionUrl";
}
