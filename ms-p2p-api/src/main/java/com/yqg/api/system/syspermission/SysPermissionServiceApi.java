package com.yqg.api.system.syspermission;

/**
 * 系统权限表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
public class SysPermissionServiceApi {
    public static final String serviceName = "service-system";
    public static final String path_userLogin = "/api-system/system/XXX";

    public static final String path_permissionItemAdd = "/api-system/system/permissionItemAdd";

    public static final String path_permissionItemEdit = "/api-system/system/permissionItemEdit";

    public static final String path_permissionList = "/api-system/system/permissionList";                      //查询权限列表

    public static final String path_permissionTreeByUserId = "/api-system/system/permissionTreeByUserId";      //通过用户id查询用户权限树
}