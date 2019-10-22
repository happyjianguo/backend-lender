package com.yqg.task.service.impl;

import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.task.service.OrderTaskService;
import org.springframework.stereotype.Component;

@Component
public class OrderTaskServiceFallbackImpl implements OrderTaskService {


    @Override
    public BaseResponse path_batchLoan() throws Exception {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse path_expireOrder() throws Exception {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse batchLoanWating() throws Exception {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
