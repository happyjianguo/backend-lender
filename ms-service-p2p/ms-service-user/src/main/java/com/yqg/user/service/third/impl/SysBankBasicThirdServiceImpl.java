package com.yqg.user.service.third.impl;

import com.yqg.api.system.sysbankbasicinfo.bo.SysBankBasicInfoBo;
import com.yqg.api.system.sysbankbasicinfo.ro.SysBankBasicInfoRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.service.third.SysBankBasicThirdService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public class SysBankBasicThirdServiceImpl implements SysBankBasicThirdService {

    @Override
    public BaseResponse<SysBankBasicInfoBo> bankInfoByCode(@RequestBody SysBankBasicInfoRo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
