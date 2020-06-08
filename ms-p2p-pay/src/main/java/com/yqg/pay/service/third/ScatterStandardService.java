package com.yqg.pay.service.third;

import com.yqg.api.order.OrderOrderServiceApi;
import com.yqg.api.order.orderorder.ro.OrderSuccessRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.pay.service.third.impl.ScatterStandardFallbackImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 订单
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
//@FeignClient(value = OrderOrderServiceApi.serviceName, fallback = ScatterStandardFallbackImpl.class)
public interface ScatterStandardService {

//    @PostMapping(value = OrderOrderServiceApi.path_repaySuccess+"/{creditorNo}")
    public BaseResponse repaySuccess(@PathVariable(value = "creditorNo") String creditorNo, @RequestBody OrderSuccessRo ro) throws BusinessException;

//    @PostMapping(value = OrderOrderServiceApi.path_repayFail+"/{creditorNo}")
    public BaseResponse repayFail(@PathVariable(value = "creditorNo") String creditorNo) throws BusinessException;


//    @PostMapping(value = OrderOrderServiceApi.path_serviceFeeSuccess+"/{creditorNo}")
    public BaseResponse serviceFeeSuccess(@PathVariable(value = "creditorNo") String creditorNo) throws Exception;





    }