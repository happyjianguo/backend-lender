package com.yqg.user.service.third;

import com.yqg.api.system.sysuser.SysUserServiceApi;
import com.yqg.api.system.sysuser.bo.SysUserBo;
import com.yqg.api.system.sysuser.ro.SysUserEditRo;
import com.yqg.api.user.useraccount.ro.UserAccountNotSessionRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.service.third.impl.SysUserServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: hyy
 * @Date: 2019/6/26 15:41
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@FeignClient(value = SysUserServiceApi.serviceName, fallback = SysUserServiceImpl.class)
public interface SysUserService {
    @PostMapping(value = SysUserServiceApi.path_getSysUserById)
    BaseResponse<SysUserBo> getSysUserById(@RequestBody UserAccountNotSessionRo ro) throws BusinessException;

}
