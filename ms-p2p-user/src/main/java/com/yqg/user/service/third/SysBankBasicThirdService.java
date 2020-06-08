package com.yqg.user.service.third;

import com.yqg.api.system.sysbankbasicinfo.SysBankBasicInfoServiceApi;
import com.yqg.api.system.sysbankbasicinfo.bo.SysBankBasicInfoBo;
import com.yqg.api.system.sysbankbasicinfo.ro.SysBankBasicInfoRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.service.third.impl.SysBankBasicThirdServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//
//@FeignClient(value = SysBankBasicInfoServiceApi.serviceName, fallback = SysBankBasicThirdServiceImpl.class)
public interface SysBankBasicThirdService {
//    @PostMapping(value = SysBankBasicInfoServiceApi.path_bankInfoByCode)
    public BaseResponse<SysBankBasicInfoBo> bankInfoByCode(@RequestBody SysBankBasicInfoRo ro) throws BusinessException;
}
