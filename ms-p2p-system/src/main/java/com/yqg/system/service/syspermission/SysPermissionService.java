package com.yqg.system.service.syspermission;

import com.yqg.api.system.syspermission.bo.SysPermissionListBo;
import com.yqg.api.system.syspermission.ro.SysPermissionRo;
import com.yqg.common.dynamicdata.TargetDataSource;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.core.BaseService;
import com.yqg.system.entity.SysPermission;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统权限表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
public interface SysPermissionService extends BaseService<SysPermission> {

    /*添加权限项*/
    void addPermissionItem(SysPermissionRo ro) throws BusinessException;

    /*修改权限项*/
    void permissionItemEdit(SysPermissionRo ro) throws BusinessException;

    /*权限列表查询*/
    List<SysPermissionListBo> permissionList(List<String> permissionsIds) throws BusinessException;

    /*通过用户id查询用有的权限*/
    List<SysPermissionListBo> permissionTreeByUserId(String userId) throws BusinessException;
}