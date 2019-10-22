package com.yqg.order.service.third.impl;

import com.yqg.api.system.systhirdlogs.bo.SysThirdLogsBo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.service.third.SysThirdLogsService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by zhaoruifeng on 2018/9/5.
 */
@Component
public class SysThirdLogsServiceImpl implements SysThirdLogsService {
    @Override
    public BaseResponse<SysThirdLogsBo> addThirdLog(@RequestBody SysThirdLogsBo ro) throws BusinessException {
        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
    }
}
