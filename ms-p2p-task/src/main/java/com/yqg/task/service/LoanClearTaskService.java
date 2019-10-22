package com.yqg.task.service;

import com.yqg.api.order.OrderOrderServiceApi;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.task.service.impl.OrderTaskServiceFallbackImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Remark:
 * Created by huwei on 19.6.12.
 */
@FeignClient(value = OrderOrderServiceApi.serviceName, fallback = OrderTaskServiceFallbackImpl.class)
public interface LoanClearTaskService {

    @ApiOperation("机构投资人清分收入处理定时任务")
    @PostMapping(value = OrderOrderServiceApi.path_handleBreachDisburse)
    public BaseResponse handleBreachDisburse() throws Exception ;
}
