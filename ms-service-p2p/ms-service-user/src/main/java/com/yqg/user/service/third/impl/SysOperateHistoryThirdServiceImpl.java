package com.yqg.user.service.third.impl;

import com.yqg.api.system.sysoperatehistory.ro.SysOperateHistoryAddRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.service.third.SysOperateHistoryThirdService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public class SysOperateHistoryThirdServiceImpl implements SysOperateHistoryThirdService {
    @Override
    public BaseResponse addOperateHistory(@RequestBody SysOperateHistoryAddRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
