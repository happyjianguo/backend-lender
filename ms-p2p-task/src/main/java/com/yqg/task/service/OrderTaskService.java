package com.yqg.task.service;

import com.yqg.api.order.OrderOrderServiceApi;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.task.service.impl.OrderTaskServiceFallbackImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 支付结果任务
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@FeignClient(value = OrderOrderServiceApi.serviceName, fallback = OrderTaskServiceFallbackImpl.class)
public interface OrderTaskService {

    @ApiOperation("放款定时任务")
    @PostMapping(value = OrderOrderServiceApi.path_batchLoan)
    public BaseResponse path_batchLoan() throws Exception ;


    @ApiOperation("放款失效订单任务")
    @PostMapping(value = OrderOrderServiceApi.path_expireOrder)
    public BaseResponse path_expireOrder() throws Exception ;

    @ApiOperation(value = "散标放款处理中--定时任务", notes = "散标放款处理中--定时任务")
    @PostMapping(value = OrderOrderServiceApi.path_batchLoanWating)
    public BaseResponse batchLoanWating() throws Exception;

}