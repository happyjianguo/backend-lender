package com.yqg.order.service.third.impl;

import com.yqg.api.user.useraccounthistory.ro.UserAccountHistoryTotalRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.service.third.UserAccountHistoryService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Remark:
 * Created by huwei on 19.5.24.
 */
@Component
public class UserAccountHistoryServiceFallBackImpl implements UserAccountHistoryService {
    @Override
    public BaseResponse getUserAccountHistoryTotal(@RequestBody UserAccountHistoryTotalRo ro) throws Exception {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
