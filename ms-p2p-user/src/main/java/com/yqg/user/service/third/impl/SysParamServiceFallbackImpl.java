package com.yqg.user.service.third.impl;

import com.yqg.api.system.sysparam.bo.SysParamBo;
import com.yqg.api.system.sysparam.ro.SysParamRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.service.third.SysParamService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public class SysParamServiceFallbackImpl implements SysParamService {


    @Override
    public BaseResponse<SysParamBo> sysValueByKey(@RequestBody SysParamRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
