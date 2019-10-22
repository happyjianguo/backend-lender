package com.yqg.system.controllor;

import com.yqg.api.system.sysrole.SysRoleServiceApi;
import com.yqg.api.system.sysrole.ro.ManSysRoleRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.system.service.sysrole.SysRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * 系统角色表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@RestController
public class SysRoleController extends BaseControllor {
    @Autowired
    SysRoleService sysroleService;

    @ApiOperation(value = "添加用户角色", notes = "添加用户角色")
    @PostMapping(value = SysRoleServiceApi.path_addSysRole)
    public BaseResponse<Object> addSysRole(@RequestBody ManSysRoleRo ro) throws BusinessException {
        this.sysroleService.addSysRole(ro);
        return new BaseResponse<>().successResponse();
    }


    @ApiOperation(value = "角色列表", notes = "角色列表")
    @PostMapping(value = SysRoleServiceApi.path_sysRoleList)
    public BaseResponse<Object> sysRolesList(@RequestBody BaseSessionIdRo ro) throws BusinessException {
        return new BaseResponse<>().successResponse(this.sysroleService.sysRolesList());
    }


    @ApiOperation(value = "修改用户角色", notes = "修改用户角色")
    @PostMapping(value = SysRoleServiceApi.path_sysRoleEdit)
    public BaseResponse<Object> sysRoleEdit(@RequestBody ManSysRoleRo ro) throws BusinessException {
        this.sysroleService.sysRoleEdit(ro);
        return new BaseResponse<>().successResponse();
    }

    @ApiOperation(value = "查询与角色关联的权限url", notes = "查询与角色关联的权限url")
    @PostMapping(value = SysRoleServiceApi.path_rolePermissionCheckList)
    public BaseResponse<Object> rolePermissionCheckList(@RequestBody ManSysRoleRo ro) throws BusinessException {
        return new BaseResponse<>().successResponse(this.sysroleService.rolePermissionCheckList(ro));
    }
}