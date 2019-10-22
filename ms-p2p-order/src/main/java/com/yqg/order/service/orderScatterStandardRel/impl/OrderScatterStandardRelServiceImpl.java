package com.yqg.order.service.orderScatterStandardRel.impl;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.order.dao.OrderScatterStandardRelDao;
import com.yqg.order.entity.OrderScatterStandardRel;
import com.yqg.order.service.orderScatterStandardRel.OrderScatterStandardRelService;
import com.yqg.order.service.orderorder.OrderOrderService;
import com.yqg.order.service.scatterstandard.ScatterstandardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 债权包表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-15 18:21:38
 */
@Service("orderScatterStandardRelService")
@Slf4j
public class OrderScatterStandardRelServiceImpl implements OrderScatterStandardRelService {
    @Autowired
    protected OrderScatterStandardRelDao orderScatterStandardRelDao;
    @Autowired
    protected ScatterstandardService scatterstandardService;
    @Autowired
    protected OrderOrderService orderOrderService;


    public Page<OrderScatterStandardRel> findForPage(OrderScatterStandardRel t, Pageable pageable) throws BusinessException {
        return this.orderScatterStandardRelDao.findForPage(t, pageable);
    }


    @Override
    public String addOne(OrderScatterStandardRel orderScatterStandardRel) throws BusinessException {
        return orderScatterStandardRelDao.addOne(orderScatterStandardRel);
    }

    @Override
    public OrderScatterStandardRel findById(String id) throws BusinessException {
        return this.orderScatterStandardRelDao.findOneById(id, new OrderScatterStandardRel());
    }

    @Override
    public <E> E findById(String s, Class<E> aClass) throws BusinessException {
        OrderScatterStandardRel orderScatterStandardRel = findById(s);
        E bo = BeanCoypUtil.copyToNewObject(orderScatterStandardRel, aClass);
        return bo;
    }


    @Override
    public OrderScatterStandardRel findOne(OrderScatterStandardRel orderScatterStandardRel) throws BusinessException {
        return orderScatterStandardRelDao.findOne(orderScatterStandardRel);
    }

    @Override
    public <E> E findOne(OrderScatterStandardRel entity, Class<E> aClass) throws BusinessException {
        OrderScatterStandardRel orderScatterStandardRel = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(orderScatterStandardRel, aClass);
        return bo;
    }


    @Override
    public List<OrderScatterStandardRel> findList(OrderScatterStandardRel orderScatterStandardRel) throws BusinessException {
        return orderScatterStandardRelDao.findForList(orderScatterStandardRel);
    }

    @Override
    public <E> List<E> findList(OrderScatterStandardRel orderScatterStandardRel, Class<E> aClass) throws BusinessException {
        return null;
    }


    @Override
    public void updateOne(OrderScatterStandardRel orderScatterStandardRel) throws BusinessException {
        orderScatterStandardRelDao.updateOne(orderScatterStandardRel);
    }

    @Override
    public void deleteById(String s) throws BusinessException {
        orderScatterStandardRelDao.delete(s);
    }


    @Override
    public void deleteOne(OrderScatterStandardRel orderScatterStandardRel) throws BusinessException {
        orderScatterStandardRelDao.deleteOne(orderScatterStandardRel);
    }


}