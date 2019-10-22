package com.yqg.system.service.sysuser;

import com.yqg.api.system.sysuser.bo.SysUserBo;
import com.yqg.api.system.sysuser.bo.SysUserImageBo;
import com.yqg.api.system.sysuser.bo.SysUserLoginBo;
import com.yqg.api.system.sysuser.ro.*;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.core.BaseService;
import com.yqg.system.entity.SysUser;

/**
 * 系统用户表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-09-20 15:52:10
 */
public interface SysUserService extends BaseService<SysUser> {

    /**
     * 管理后台用户登录
     * */
    SysUserLoginBo sysUserLogin(SysUserLoginRo ro) throws BusinessException;

    /**
     * 管理后台用户密码修改
     * */
    void editSysUserPassword(SysUserChangePasswdRo ro) throws BusinessException;

    /**
     * 管理后台用户密码重置
     * */
    void resetSysUserPassword(BaseSessionIdRo ro) throws BusinessException;

    /*添加用户*/
    SysUser addSysUser(SysUserEditRo ro) throws BusinessException;

    /*修改用户*/
    SysUser editSysUser(SysUserEditRo ro) throws BusinessException;

    /*用户列表查询*/
    BasePageResponse<SysUserBo> sysUserList(SysUserSearchRo ro) throws BusinessException;


    SysUserImageBo lookUserImage(SysUserImageRo ro)throws BusinessException;
}