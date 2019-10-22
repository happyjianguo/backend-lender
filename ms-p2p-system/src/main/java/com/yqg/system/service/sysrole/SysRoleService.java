package com.yqg.system.service.sysrole;

import com.yqg.api.system.syspermission.bo.SysPermissionListBo;
import com.yqg.api.system.sysrole.ro.ManSysRoleRo;
import com.yqg.common.dynamicdata.TargetDataSource;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.core.BaseService;
import com.yqg.system.entity.SysRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统角色表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
public interface SysRoleService extends BaseService<SysRole> {

    /*角色列表*/
    List<SysRole> sysRolesList() throws BusinessException;

    /*添加用户角色*/
    void addSysRole(ManSysRoleRo ro) throws BusinessException;

    /*修改用户角色*/
    void sysRoleEdit(ManSysRoleRo ro) throws BusinessException;

    /*查询与角色关联的权限url*/
    List<SysPermissionListBo> rolePermissionCheckList(ManSysRoleRo request)
            throws BusinessException;
}