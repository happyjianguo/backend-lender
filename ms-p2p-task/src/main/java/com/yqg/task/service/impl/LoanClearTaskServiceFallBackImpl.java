package com.yqg.task.service.impl;

import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.task.service.LoanClearTaskService;

/**
 * Remark:
 * Created by huwei on 19.6.12.
 */
public class LoanClearTaskServiceFallBackImpl implements LoanClearTaskService {
    @Override
    public BaseResponse handleBreachDisburse() throws Exception {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
