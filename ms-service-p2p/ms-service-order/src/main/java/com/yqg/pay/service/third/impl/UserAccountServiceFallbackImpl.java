package com.yqg.pay.service.third.impl;

import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useraccount.ro.UserAccountNotSessionRo;
import com.yqg.api.user.useraccount.ro.UseraccountRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.pay.service.third.UserAccountService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
public class UserAccountServiceFallbackImpl implements UserAccountService {


    @Override
    public BaseResponse subtractScatterstandardBlance(@RequestBody UseraccountRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse addScatterstandardBlance(@RequestBody UserAccountNotSessionRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse<UserAccountBo> selectUserAccount(@RequestBody UseraccountRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public List<UserAccountBo> selectAllUserAccount() throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

//    @Override
//    public BaseResponse<UserAccountBo> selectScatterstandard(@RequestBody UseraccountRo ro) throws BusinessException {
//        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
//    }

    @Override
    public BaseResponse<UserAccountBo> addUserDepositBlance(@RequestBody UseraccountRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse addUserCurrentBlance(@RequestBody UseraccountRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse dingqi2dongjie(@RequestBody UseraccountRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse huoqi2dingqi(@RequestBody UseraccountRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse huoqi2sanbiao(@RequestBody UseraccountRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public void dongjie2dingqi(@RequestBody UseraccountRo ro) throws BusinessException {
       // UserAccount

    }

    @Override
    public BaseResponse returnMoneyUserDepositLockBlance(UseraccountRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
