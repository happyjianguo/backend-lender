package com.yqg.pay.service.third;

import com.yqg.api.system.sysdic.SysDicServiceApi;
import com.yqg.api.system.sysdic.ro.SysDicRo;
import com.yqg.api.system.sysdicitem.bo.SysDicItemBo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.pay.service.third.impl.SysDictServiceFallbackImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 系统参数
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@FeignClient(value = SysDicServiceApi.serviceName, fallback = SysDictServiceFallbackImpl.class)
public interface SysDictService {
//    @PostMapping(value = SysDicServiceApi.path_dicItemListByCode)
//    BaseResponse<List> sysValueByKey(@RequestBody SysDicRo ro) throws BusinessException;

    @PostMapping(value = SysDicServiceApi.path_dicItemBoListByDicCode)
    public BaseResponse<List<SysDicItemBo>> dicItemBoListByDicCode(@RequestBody SysDicRo ro ) throws BusinessException;
}