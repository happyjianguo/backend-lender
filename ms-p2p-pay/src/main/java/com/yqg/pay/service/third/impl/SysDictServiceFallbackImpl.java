package com.yqg.pay.service.third.impl;

import com.yqg.api.system.sysdic.ro.SysDicRo;
import com.yqg.api.system.sysdicitem.bo.SysDicItemBo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.pay.service.third.SysDictService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
public class SysDictServiceFallbackImpl implements SysDictService {


//    @Override
//    public BaseResponse<List> sysValueByKey(@RequestBody SysDicRo ro) throws BusinessException {
//        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
//    }

    @Override
    public BaseResponse<List<SysDicItemBo>> dicItemBoListByDicCode(@RequestBody SysDicRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
