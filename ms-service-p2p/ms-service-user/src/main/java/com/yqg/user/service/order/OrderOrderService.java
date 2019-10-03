package com.yqg.user.service.order;

import com.yqg.api.order.orderorder.OrderOrderServiceApi;
import com.yqg.api.order.orderorder.bo.OrderOrderBo;
import com.yqg.api.order.orderorder.bo.OrderSizeBo;
import com.yqg.api.order.orderorder.ro.OrderOrderPageRo;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by liyixing on 2018/9/17.
 */
@FeignClient(value = OrderOrderServiceApi.serviceName, fallback = OrderOrderServiceImpl.class)
public interface OrderOrderService {

    @PostMapping(value = OrderOrderServiceApi.path_selectUsrOrderSize)
    String selectUsrOrderSize(OrderOrderPageRo orderOrderPageRo) throws BusinessException;
}
