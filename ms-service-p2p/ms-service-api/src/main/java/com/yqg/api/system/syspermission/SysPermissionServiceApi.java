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
    public static final String path_userLogin = "/system/XXX";

    public static final String path_permissionItemAdd = "/system/permissionItemAdd";

    public static final String path_permissionItemEdit = "/system/permissionItemEdit";

    public static final String path_permissionList = "/system/permissionList";                      //查询权限列表

    public static final String path_permissionTreeByUserId = "/system/permissionTreeByUserId";      //通过用户id查询用户权限树
}