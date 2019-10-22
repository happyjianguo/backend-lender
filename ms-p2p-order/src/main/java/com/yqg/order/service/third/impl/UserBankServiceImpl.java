package com.yqg.order.service.third.impl;

import com.yqg.api.user.userbank.bo.UserBankBo;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.service.third.UserBankService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public class UserBankServiceImpl implements UserBankService {

    @Override
    public BaseResponse<UserBankBo> getUserBankInfo(@RequestBody UserBankRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
