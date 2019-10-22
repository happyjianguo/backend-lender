package com.yqg.order.service.third;

import com.yqg.api.system.sysparam.SysParamServiceApi;
import com.yqg.api.system.sysparam.bo.SysParamBo;
import com.yqg.api.system.sysparam.ro.SysParamRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.service.third.impl.SysParamServiceFallbackImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 系统参数
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@FeignClient(value = SysParamServiceApi.serviceName, fallback = SysParamServiceFallbackImpl.class)
public interface SysParamService {
    @PostMapping(value = SysParamServiceApi.path_sysParamValueBykey)
    BaseResponse<SysParamBo> sysValueByKey(@RequestBody SysParamRo ro) throws BusinessException;
}