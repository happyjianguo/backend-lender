package com.yqg.order.service.creditorpackage.impl;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.common.utils.DateUtils;
import com.yqg.order.dao.CreditorpackageDao;
import com.yqg.order.entity.Creditorpackage;
import com.yqg.order.entity.Productconf;
import com.yqg.order.enums.OrderExceptionEnums;
import com.yqg.order.service.creditorpackage.CreditorpackageService;
import com.yqg.order.service.productconf.ProductconfService;
import com.yqg.pay.service.PayCommonServiceImpl;
import com.yqg.pay.util.RadomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 债权包表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-15 18:21:38
 */
@Service("creditorPackageService")
public class CreditorpackageServiceImpl extends PayCommonServiceImpl implements CreditorpackageService {
    @Autowired
    protected CreditorpackageDao creditorpackageDao;
    @Autowired
    protected ProductconfService productconfService;
    @Transactional
    public void updateCreditorpackage(Creditorpackage ro) throws BusinessException {
        Creditorpackage crd1 = new Creditorpackage();
        crd1.setCode(ro.getCode());
        Creditorpackage packageOne =creditorpackageDao.findOne(crd1);

        packageOne.setStatus(ro.getStatus());
        packageOne.setAmount(ro.getAmount());
        creditorpackageDao.updateOne(packageOne);
    }

    @Override
    public List<Creditorpackage> queryCreditorpackage(Creditorpackage ro) throws Exception {
        Creditorpackage crd = new Creditorpackage();
        if(!StringUtils.isEmpty(ro.getProductUuid())){
            crd.setProductUuid(ro.getProductUuid());
        }
        if(!StringUtils.isEmpty(ro.getStatus())){
            crd.setStatus(ro.getStatus());
        }
        if(!StringUtils.isEmpty(ro.getCode())){
            crd.setCode(ro.getCode());
        }
        List<Creditorpackage> packageOne =creditorpackageDao.findForList(crd);
        return packageOne;
    }


    @Transactional
    public Creditorpackage createCreditorpackage(Creditorpackage ro) throws Exception {
        Productconf prod = productconfService.findById(ro.getProductUuid());
        if(prod == null){
            throw new BusinessException(OrderExceptionEnums.PRODUCT_ERROR);
        }

        //查询产品列表
        Creditorpackage crd = new Creditorpackage();
        crd.setProductUuid(prod.getId());
        crd.setCode(prod.getRemark()+ RadomUtil.getRandom(6));
        crd.setStatus(0);//初始化
        crd.setCreateTime(new Date());
        crd.setUpdateTime(new Date());
        crd.setEndTime(DateUtils.addDate(new Date(),prod.getBorrowingTerm()));
        creditorpackageDao.addOne(crd);
        /*if(creditorpackageDao.findOne(crd)!=null){
            crd.setCode(prod.getRemark()+ RadomUtil.getRandom(5));
        }*/
        Creditorpackage crd1 = new Creditorpackage();
        crd1.setCode(crd.getCode());
        Creditorpackage packageOne =creditorpackageDao.findOne(crd1);

        return packageOne;
    }



    @Override
    public Creditorpackage findById(String id) throws BusinessException {
        return this.creditorpackageDao.findOneById(id, new Creditorpackage());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        Creditorpackage creditorpackage = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(creditorpackage, boClass);
        return bo;
    }

    @Override
    public Creditorpackage findOne(Creditorpackage entity) throws BusinessException {
        return this.creditorpackageDao.findOne(entity);
    }

    @Override
    public <E> E findOne(Creditorpackage entity, Class<E> boClass) throws BusinessException {
        Creditorpackage creditorpackage = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(creditorpackage, boClass);
        return bo;
    }


    @Override
    public List<Creditorpackage> findList(Creditorpackage entity) throws BusinessException {
        return this.creditorpackageDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(Creditorpackage entity, Class<E> boClass) throws BusinessException {
        List<Creditorpackage> creditorpackageList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (Creditorpackage creditorpackage : creditorpackageList) {
            E bo = BeanCoypUtil.copyToNewObject(creditorpackage, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(Creditorpackage entity) throws BusinessException {
        return creditorpackageDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        creditorpackageDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(Creditorpackage entity) throws BusinessException {
        this.creditorpackageDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(Creditorpackage entity) throws BusinessException {
        this.creditorpackageDao.updateOne(entity);
    }

}