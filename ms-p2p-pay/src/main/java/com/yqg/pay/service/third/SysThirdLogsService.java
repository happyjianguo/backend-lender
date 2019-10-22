package com.yqg.pay.service.third;

import com.yqg.api.system.sysparam.SysParamServiceApi;
import com.yqg.api.system.sysparam.bo.SysParamBo;
import com.yqg.api.system.sysparam.ro.SysParamRo;
import com.yqg.api.system.systhirdlogs.SysThirdLogsServiceApi;
import com.yqg.api.system.systhirdlogs.bo.SysThirdLogsBo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.pay.service.third.impl.SysParamServiceFallbackImpl;
import com.yqg.pay.service.third.impl.SysThirdLogsServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by zhaoruifeng on 2018/9/5.
 */
@FeignClient(value = SysThirdLogsServiceApi.serviceName, fallback = SysThirdLogsServiceImpl.class)
public interface SysThirdLogsService {
    @PostMapping(value = SysThirdLogsServiceApi.path_addThirdLog)
    BaseResponse<SysThirdLogsBo> addThirdLog(@RequestBody SysThirdLogsBo ro) throws BusinessException;
}
