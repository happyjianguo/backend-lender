package com.yqg.system.controllor;

import com.yqg.api.system.sysuser.SysUserServiceApi;
import com.yqg.api.system.sysuser.bo.SysUserBo;
import com.yqg.api.system.sysuser.bo.SysUserLoginBo;
import com.yqg.api.system.sysuser.ro.SysUserChangePasswdRo;
import com.yqg.api.system.sysuser.ro.SysUserEditRo;
import com.yqg.api.system.sysuser.ro.SysUserLoginRo;
import com.yqg.api.system.sysuser.ro.SysUserSearchRo;
import com.yqg.api.user.useraccount.ro.UserAccountNotSessionRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.system.entity.SysUser;
import com.yqg.system.service.sysuser.SysUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.yqg.common.utils.BeanCoypUtil.copyToNewObject;


/**
 * 系统用户表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-09-20 15:52:10
 */
@RestController
public class SysUserController extends BaseControllor {
    @Autowired
    SysUserService sysUserService;

    @NotNeedLogin
    @ApiOperation(value = "管理后台用户登录", notes = "管理后台用户登录")
    @PostMapping(value = SysUserServiceApi.path_sysUserLogin)
    public BaseResponse<SysUserLoginBo> sysUserLogin(@RequestBody SysUserLoginRo ro) throws BusinessException {
        BaseResponse<SysUserLoginBo> response = this.sysUserService.sysUserLogin(ro);
        return response;
    }

    @ApiOperation(value = "修改管理后台登录密码", notes = "修改管理后台登录密码")
    @PostMapping(value = SysUserServiceApi.path_editSysUserPassword)
    public BaseResponse<SysUserLoginBo> sysUserLogin(@RequestBody SysUserChangePasswdRo ro) throws BusinessException {
        this.sysUserService.editSysUserPassword(ro);
        return new BaseResponse<SysUserLoginBo>().successResponse();
    }

    @ApiOperation(value = "修改管理后台登录密码", notes = "修改管理后台登录密码")
    @PostMapping(value = SysUserServiceApi.path_resetSysUserPassword)
    public BaseResponse<SysUserLoginBo> sysUserLogin(@RequestBody BaseSessionIdRo ro) throws BusinessException {
        this.sysUserService.resetSysUserPassword(ro);
        return new BaseResponse<SysUserLoginBo>().successResponse();
    }

    @ApiOperation(value = "用户列表查询", notes = "用户列表查询")
    @PostMapping(value = SysUserServiceApi.path_sysUserList)
    public BaseResponse<BasePageResponse<SysUserBo>> sysUserList(@RequestBody SysUserSearchRo ro) throws BusinessException {
        return new BaseResponse<BasePageResponse<SysUserBo>>().successResponse(this.sysUserService.sysUserList(ro));
    }

    @ApiOperation(value = "后台添加新用户", notes = "后台添加新用户")
    @PostMapping(value = SysUserServiceApi.path_sysUserAdd)
    public BaseResponse<Object> sysUserLogin(@RequestBody SysUserEditRo ro) throws BusinessException {
        this.sysUserService.addSysUser(ro);
        return new BaseResponse<Object>().successResponse();
    }

    @ApiOperation(value = "修改用户", notes = "修改用户")
    @PostMapping(value = SysUserServiceApi.path_sysUserEdit)
    public BaseResponse<Object> editSysUser(@RequestBody SysUserEditRo ro) throws BusinessException {
        this.sysUserService.editSysUser(ro);
        return new BaseResponse<Object>().successResponse();
    }

    @NotNeedLogin
    @ApiOperation(value = "查询用户", notes = "查询用户")
    @PostMapping(value = SysUserServiceApi.path_getSysUserById)
    public BaseResponse<SysUserBo> getSysUserById(@RequestBody UserAccountNotSessionRo ro) throws BusinessException {
        SysUser sysUser = this.sysUserService.findById(ro.getUserId());
        SysUserBo sysUserBo =  BeanCoypUtil.copyToNewObject(sysUser,SysUserBo.class);
        logger.info("查询结果{}",sysUserBo);
        return new BaseResponse<SysUserBo>().successResponse(sysUserBo);
    }
}