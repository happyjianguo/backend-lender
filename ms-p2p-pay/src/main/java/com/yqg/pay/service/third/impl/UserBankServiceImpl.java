package com.yqg.pay.service.third.impl;

import com.yqg.api.user.userbank.bo.UserBankBo;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.api.user.useruser.ro.UserAuthBankStatusRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.pay.service.third.UserBankService;
import com.yqg.pay.service.third.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public class UserBankServiceImpl implements UserBankService {

    @Override
    public BaseResponse<UserBankBo> getUserBankInfo(@RequestBody UserBankRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
