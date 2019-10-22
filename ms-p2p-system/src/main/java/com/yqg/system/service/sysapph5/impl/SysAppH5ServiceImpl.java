package com.yqg.system.service.sysapph5.impl;

import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.system.dao.SysAppH5Dao;
import com.yqg.system.entity.SysAppH5;
import com.yqg.system.service.SysCommonServiceImpl;
import com.yqg.system.service.sysapph5.SysAppH5Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * app H5 url集合表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Service("sysAppH5Service")
public class SysAppH5ServiceImpl extends SysCommonServiceImpl implements SysAppH5Service {
    @Autowired
    protected SysAppH5Dao sysAppH5Dao;


    @Override
    public SysAppH5 findById(String id) throws BusinessException {
        return this.sysAppH5Dao.findOneById(id, new SysAppH5());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        SysAppH5 sysapph5 = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(sysapph5, boClass);
        return bo;
    }

    @Override
    public SysAppH5 findOne(SysAppH5 entity) throws BusinessException {
        return this.sysAppH5Dao.findOne(entity);
    }

    @Override
    public <E> E findOne(SysAppH5 entity, Class<E> boClass) throws BusinessException {
        SysAppH5 sysapph5 = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(sysapph5, boClass);
        return bo;
    }


    @Override
    public List<SysAppH5> findList(SysAppH5 entity) throws BusinessException {
        return this.sysAppH5Dao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(SysAppH5 entity, Class<E> boClass) throws BusinessException {
        List<SysAppH5> sysapph5List = findList(entity);
        List<E> boList = new ArrayList<>();
        for (SysAppH5 sysapph5 : sysapph5List) {
            E bo = BeanCoypUtil.copyToNewObject(sysapph5, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(SysAppH5 entity) throws BusinessException {
        return sysAppH5Dao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        sysAppH5Dao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(SysAppH5 entity) throws BusinessException {
        this.sysAppH5Dao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(SysAppH5 entity) throws BusinessException {
        this.sysAppH5Dao.updateOne(entity);
    }

}