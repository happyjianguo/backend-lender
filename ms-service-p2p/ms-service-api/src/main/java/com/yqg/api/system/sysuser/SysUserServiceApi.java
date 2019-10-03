package com.yqg.api.system.sysuser;

/**
 * 系统用户表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-09-20 15:52:10
 */
public class SysUserServiceApi {
    public static final String serviceName = "service-system";

    public static final String path_sysUserLogin = "/system/userLogin";

    public static final String path_editSysUserPassword = "/system/editSysUserPassword";

    public static final String path_resetSysUserPassword = "/system/resetSysUserPassword";

    public static final String path_sysUserList = "/system/sysUserList";      //系统用户列表

    public static final String path_sysUserAdd = "/system/sysUserAdd";      //后台添加新用户

    public static final String path_sysUserEdit = "/system/sysUserEdit";      //后台添加新用户
}