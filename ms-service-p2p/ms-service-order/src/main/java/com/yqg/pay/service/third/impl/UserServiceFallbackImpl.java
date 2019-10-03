package com.yqg.pay.service.third.impl;

import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.ro.UserAuthBankStatusRo;
import com.yqg.api.user.useruser.ro.UserReq;
import com.yqg.api.user.useruser.ro.UserTypeSearchRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.pay.service.third.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
public class UserServiceFallbackImpl implements UserService {


    @Override
    public BaseResponse<UserBankAuthStatus> userAuthBankInfo(@RequestBody UserAuthBankStatusRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse<List<UserBo>> userListByType(@RequestBody UserTypeSearchRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse findUserById(@RequestBody UserReq ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse<UserBo> findOneByMobileOrId(@RequestBody UserReq ro) throws BusinessException{
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse<UserBo> findOneByRealNameOrId(@RequestBody UserReq ro) throws BusinessException{
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
