package com.yqg.system.controllor;

import com.yqg.common.core.BaseControllor;
import com.yqg.system.service.sysrolepermission.SysRolePermissionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * 系统角色权限表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@RestController
public class SysRolePermissionController extends BaseControllor {
    @Autowired
    SysRolePermissionService sysrolepermissionService;


}