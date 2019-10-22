package com.yqg.pay.service.third.impl;

import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.pay.service.third.ScatterStandardService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class ScatterStandardFallbackImpl implements ScatterStandardService {

    @Override
    public BaseResponse repaySuccess(@PathVariable(value = "creditorNo") String creditorNo) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse repayFail(@PathVariable(value = "creditorNo") String creditorNo) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse serviceFeeSuccess(@PathVariable(value = "creditorNo") String creditorNo) throws Exception {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

}
