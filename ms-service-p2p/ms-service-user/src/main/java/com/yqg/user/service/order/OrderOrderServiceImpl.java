package com.yqg.user.service.order;

import com.yqg.api.order.orderorder.bo.OrderOrderBo;
import com.yqg.api.order.orderorder.bo.OrderSizeBo;
import com.yqg.api.order.orderorder.ro.OrderOrderPageRo;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import org.springframework.stereotype.Component;

/**
 * Created by liyixing on 2018/9/17.
 */
@Component
public class OrderOrderServiceImpl implements OrderOrderService {

    @Override
    public String selectUsrOrderSize(OrderOrderPageRo orderOrderPageRo) throws BusinessException {
        return null;
    }
}
