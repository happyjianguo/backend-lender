package com.yqg.order.service.productconf.impl;

import com.yqg.api.order.productconf.ro.ProductconfRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.order.dao.ProductconfDao;
import com.yqg.order.entity.Productconf;
import com.yqg.order.service.productconf.ProductconfService;
import com.yqg.pay.service.PayCommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 投资产品表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-14 14:45:36
 */
@Service("productConfService")
public class ProductconfServiceImpl extends PayCommonServiceImpl implements ProductconfService {
    @Autowired
    protected ProductconfDao productconfDao;

    @Override
    public List<Productconf> productconfList(ProductconfRo ro) throws Exception{
        Productconf pf = new Productconf();

        return this.productconfDao.findForList(pf);

    }


    @Override
    public Productconf findById(String id) throws BusinessException {
        return this.productconfDao.findOneById(id, new Productconf());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        Productconf productconf = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(productconf, boClass);
        return bo;
    }

    @Override
    public Productconf findOne(Productconf entity) throws BusinessException {
        return this.productconfDao.findOne(entity);
    }

    @Override
    public <E> E findOne(Productconf entity, Class<E> boClass) throws BusinessException {
        Productconf productconf = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(productconf, boClass);
        return bo;
    }


    @Override
    public List<Productconf> findList(Productconf entity) throws BusinessException {
        return this.productconfDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(Productconf entity, Class<E> boClass) throws BusinessException {
        List<Productconf> productconfList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (Productconf productconf : productconfList) {
            E bo = BeanCoypUtil.copyToNewObject(productconf, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(Productconf entity) throws BusinessException {
        return productconfDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        productconfDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(Productconf entity) throws BusinessException {
        this.productconfDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(Productconf entity) throws BusinessException {
        this.productconfDao.updateOne(entity);
    }

}