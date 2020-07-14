package com.yqg.order.service.orderScatterStandardRel;

import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.entity.OrderScatterStandardRel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 订单散标关系表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-15 18:21:38
 */
public interface OrderScatterStandardRelService extends BaseService<OrderScatterStandardRel> {
    public Page<OrderScatterStandardRel> findForPage(OrderScatterStandardRel t, Pageable pageable) throws BusinessException;


//    public List<String> getListPendingById(String id) throws BusinessException;

}