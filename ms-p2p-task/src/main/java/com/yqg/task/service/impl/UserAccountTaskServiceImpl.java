package com.yqg.task.service.impl;

import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.task.service.UserAccountTaskService;
import org.springframework.stereotype.Component;

/**
 * @Author: hyy
 * @Date: 2019/5/22 9:51
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@Component
public class UserAccountTaskServiceImpl implements UserAccountTaskService {

    @Override
    public BaseResponse autoWithdrawDeposit() throws Exception {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse autoWithdrawDepositCheck() throws Exception {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}