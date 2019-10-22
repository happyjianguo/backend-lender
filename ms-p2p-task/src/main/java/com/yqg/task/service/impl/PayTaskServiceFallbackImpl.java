package com.yqg.task.service.impl;

import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.task.service.PayTaskService;
import org.springframework.stereotype.Component;

@Component
public class PayTaskServiceFallbackImpl implements PayTaskService {


    @Override
    public BaseResponse payResult() throws Exception {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse loanResult() throws Exception {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

}
