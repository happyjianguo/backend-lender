package com.yqg.order.service.orderpackagerel.impl;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.order.dao.OrderpackagerelDao;
import com.yqg.order.entity.Orderpackagerel;
import com.yqg.order.service.orderpackagerel.OrderpackagerelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单债权包关系表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-27 15:04:24
 */
@Service("orderPackageRelService")
public class OrderpackagerelServiceImpl  implements OrderpackagerelService {
    @Autowired
    protected OrderpackagerelDao orderpackagerelDao;
    @Override
    public List<Orderpackagerel> findByPackageNo(String packageNo) throws Exception{
        Orderpackagerel orderpackagerel = new Orderpackagerel();
        orderpackagerel.setCode(packageNo);
      //  orderpackagerel.set
        List<Orderpackagerel> orderPackageList = orderpackagerelDao.findForList(orderpackagerel);
        return orderPackageList;
    }


    @Override
    public Orderpackagerel findById(String id) throws BusinessException {
        return this.orderpackagerelDao.findOneById(id, new Orderpackagerel());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        Orderpackagerel orderpackagerel = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(orderpackagerel, boClass);
        return bo;
    }

    @Override
    public Orderpackagerel findOne(Orderpackagerel entity) throws BusinessException {
        return this.orderpackagerelDao.findOne(entity);
    }

    @Override
    public <E> E findOne(Orderpackagerel entity, Class<E> boClass) throws BusinessException {
        Orderpackagerel orderpackagerel = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(orderpackagerel, boClass);
        return bo;
    }


    @Override
    public List<Orderpackagerel> findList(Orderpackagerel entity) throws BusinessException {
        return this.orderpackagerelDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(Orderpackagerel entity, Class<E> boClass) throws BusinessException {
        List<Orderpackagerel> orderpackagerelList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (Orderpackagerel orderpackagerel : orderpackagerelList) {
            E bo = BeanCoypUtil.copyToNewObject(orderpackagerel, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(Orderpackagerel entity) throws BusinessException {
        return orderpackagerelDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        orderpackagerelDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(Orderpackagerel entity) throws BusinessException {
        this.orderpackagerelDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(Orderpackagerel entity) throws BusinessException {
        this.orderpackagerelDao.updateOne(entity);
    }

}