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

    public static final String path_sysUserLogin = "/api-system/system/userLogin";

    public static final String path_editSysUserPassword = "/api-system/system/editSysUserPassword";

    public static final String path_resetSysUserPassword = "/api-system/system/resetSysUserPassword";

    public static final String path_sysUserList = "/api-system/system/sysUserList";      //系统用户列表

    public static final String path_sysUserAdd = "/api-system/system/sysUserAdd";      //后台添加新用户

    public static final String path_sysUserEdit = "/api-system/system/sysUserEdit";      //后台添加新用户

    public static final String path_sysUserImageLook = "/api-system/system/sysUserImageLook";      //查看用户图片

    public static final String path_getSysUserById = "/api-system/system/getSysUserById";
}