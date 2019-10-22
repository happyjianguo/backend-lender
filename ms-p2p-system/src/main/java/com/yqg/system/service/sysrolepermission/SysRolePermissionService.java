package com.yqg.system.service.sysrolepermission;

import com.yqg.common.dynamicdata.TargetDataSource;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.core.BaseService;
import com.yqg.system.entity.SysRolePermission;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统角色权限表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
public interface SysRolePermissionService extends BaseService<SysRolePermission> {
    /*通过roleId查询权限url列表*/
    List<SysRolePermission> SysRolePermissItemByRoleId (String roleId) throws BusinessException;

    /*删除role和权限关联关系*/
    void delSysRolePermissionItemByRoleId(String roleId) throws BusinessException;
}