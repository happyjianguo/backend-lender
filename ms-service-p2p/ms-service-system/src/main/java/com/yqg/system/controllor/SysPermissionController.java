package com.yqg.system.controllor;


import com.yqg.api.system.syspermission.SysPermissionServiceApi;
import com.yqg.api.system.syspermission.ro.SysPermissionRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.system.service.syspermission.SysPermissionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * 系统权限表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@RestController
public class SysPermissionController extends BaseControllor {
    @Autowired
    SysPermissionService syspermissionService;

    @ApiOperation(value = "添加系统权限url", notes = "添加系统权限url")
    @PostMapping(value = SysPermissionServiceApi.path_permissionItemAdd)
    public BaseResponse<Object> addPermissionItem(@RequestBody SysPermissionRo ro) throws Exception {
        this.syspermissionService.addPermissionItem(ro);
        return new BaseResponse<>().successResponse();
    }

    @ApiOperation(value = "修改权限项", notes = "修改权限项")
    @PostMapping(value = SysPermissionServiceApi.path_permissionItemEdit)
    public BaseResponse<Object> permissionItemEdit(@RequestBody SysPermissionRo ro) throws Exception {
        this.syspermissionService.permissionItemEdit(ro);
        return new BaseResponse<>().successResponse();
    }

    @ApiOperation(value = "权限列表查询", notes = "权限列表查询")
    @PostMapping(value = SysPermissionServiceApi.path_permissionList)
    public BaseResponse<Object> permissionList(@RequestBody BaseSessionIdRo ro) throws Exception {
        return new BaseResponse<>().successResponse(this.syspermissionService.permissionList(new ArrayList<>()));
    }

    @ApiOperation(value = "权限列表查询", notes = "权限列表查询")
    @PostMapping(value = SysPermissionServiceApi.path_permissionTreeByUserId)
    public BaseResponse<Object> permissionList(@RequestBody SysPermissionRo ro) throws Exception {
        return new BaseResponse<>().successResponse(this.syspermissionService.permissionTreeByUserId(ro.getId()));
    }

}