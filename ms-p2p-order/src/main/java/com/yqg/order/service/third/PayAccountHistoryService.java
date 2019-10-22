package com.yqg.order.service.third;

import com.yqg.api.pay.PayServiceApi;
import com.yqg.api.pay.payaccounthistory.PayAccountHistoryServiceApi;
import com.yqg.api.pay.payaccounthistory.ro.PayAccountHistoryRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.service.third.impl.PayServiceFallbackImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Remark:
 * Created by huwei on 19.6.27.
 */
@FeignClient(value = PayServiceApi.serviceName, fallback = PayServiceFallbackImpl.class)
public interface PayAccountHistoryService {
    @PostMapping(value = PayAccountHistoryServiceApi.path_addPayAccountHistory)
    public BaseResponse addPayAccountHistory(@RequestBody PayAccountHistoryRo payAccountHistoryRo) throws BusinessException;
}
