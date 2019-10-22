package com.yqg.user.service.third.impl;

import com.yqg.api.system.sysuser.bo.SysUserBo;
import com.yqg.api.system.sysuser.ro.SysUserEditRo;
import com.yqg.api.user.useraccount.ro.UserAccountNotSessionRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.service.third.SysUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: hyy
 * @Date: 2019/6/26 15:45
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@Component
public class SysUserServiceImpl implements SysUserService {
    @Override
    public BaseResponse<SysUserBo> getSysUserById(@RequestBody UserAccountNotSessionRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}