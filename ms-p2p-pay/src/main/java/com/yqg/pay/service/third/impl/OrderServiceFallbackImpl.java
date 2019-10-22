package com.yqg.pay.service.third.impl;

import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.pay.service.third.OrderService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class OrderServiceFallbackImpl implements OrderService {


    @Override
    public BaseResponse successOrder(@PathVariable(value = "orderNo") String orderNo) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }

    @Override
    public BaseResponse failOrder(@PathVariable(value = "orderNo") String orderNo) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
