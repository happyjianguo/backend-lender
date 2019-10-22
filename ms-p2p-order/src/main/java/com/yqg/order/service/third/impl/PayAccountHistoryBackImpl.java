package com.yqg.order.service.third.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.payaccounthistory.ro.PayAccountHistoryRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.service.third.PayAccountHistoryService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Remark:
 * Created by huwei on 19.6.27.
 */
@Component
public class PayAccountHistoryBackImpl implements PayAccountHistoryService {
    @Override
    public BaseResponse<JSONObject> addPayAccountHistory(@RequestBody PayAccountHistoryRo payAccountHistoryRo) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
