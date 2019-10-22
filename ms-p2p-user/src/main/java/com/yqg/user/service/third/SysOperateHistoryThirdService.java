package com.yqg.user.service.third;

import com.yqg.api.system.sysoperatehistory.SysOperateHistoryServiceApi;
import com.yqg.api.system.sysoperatehistory.ro.SysOperateHistoryAddRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.service.third.impl.SysBankBasicThirdServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = SysOperateHistoryServiceApi.serviceName, fallback = SysBankBasicThirdServiceImpl.class)
public interface SysOperateHistoryThirdService {
    @PostMapping(value = SysOperateHistoryServiceApi.path_addSysOperateHistory)
    public BaseResponse addOperateHistory(@RequestBody SysOperateHistoryAddRo ro) throws BusinessException;
}
