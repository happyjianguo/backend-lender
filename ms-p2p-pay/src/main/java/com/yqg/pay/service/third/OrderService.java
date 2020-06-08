package com.yqg.pay.service.third;

import com.yqg.api.order.OrderOrderServiceApi;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.pay.service.third.impl.OrderServiceFallbackImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 订单
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
//@FeignClient(value = OrderOrderServiceApi.serviceName, fallback = OrderServiceFallbackImpl.class)
public interface OrderService {

//    @PostMapping(value = OrderOrderServiceApi.path_successOrder+"/{orderNo}")
    public BaseResponse successOrder(@PathVariable(value = "orderNo") String orderNo) throws BusinessException;

//    @PostMapping(value = OrderOrderServiceApi.path_failOrder+"/{orderNo}")
    public BaseResponse failOrder(@PathVariable(value = "orderNo") String orderNo) throws BusinessException;
}