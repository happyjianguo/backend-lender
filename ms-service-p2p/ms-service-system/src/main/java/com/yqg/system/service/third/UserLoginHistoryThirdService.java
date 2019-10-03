package com.yqg.system.service.third;

import com.yqg.api.user.usrloginhistory.UsrLoginHistoryServiceApi;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.system.service.third.impl.UserLoginHistoryThirdServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = UsrLoginHistoryServiceApi.serviceName, fallback = UserLoginHistoryThirdServiceImpl.class)
public interface UserLoginHistoryThirdService {
    @PostMapping(value = UsrLoginHistoryServiceApi.path_sysUserLoginLog)
    public BaseResponse<Object> sysUserLoginHistory() throws BusinessException;
}
