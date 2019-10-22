package com.yqg.pay.service.third.impl;

import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.pay.service.third.UserAccountService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public class UserAccountServiceFallbackImpl implements UserAccountService {


    @Override
    public BaseResponse addUserCurrentBlance(@RequestBody UserAccountChangeRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse current2lock(@RequestBody UserAccountChangeRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse lock2current(@RequestBody UserAccountChangeRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse userCharge(@RequestBody UserAccountChangeRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
