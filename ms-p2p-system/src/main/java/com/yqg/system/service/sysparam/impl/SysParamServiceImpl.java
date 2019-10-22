package com.yqg.system.service.sysparam.impl;

import com.yqg.api.system.sysparam.bo.SysParamBo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.system.dao.SysParamDao;
import com.yqg.system.entity.SysParam;
import com.yqg.system.service.SysCommonServiceImpl;
import com.yqg.system.service.sysparam.SysParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统参数
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Service("sysParamService")
public class SysParamServiceImpl extends SysCommonServiceImpl implements SysParamService {
    @Autowired
    protected SysParamDao sysParamDao;


    @Override
    public SysParam findById(String id) throws BusinessException {
        return this.sysParamDao.findOneById(id, new SysParam());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        SysParam sysparam = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(sysparam, boClass);
        return bo;
    }

    @Override
    public SysParam findOne(SysParam entity) throws BusinessException {
        return this.sysParamDao.findOne(entity);
    }

    @Override
    public <E> E findOne(SysParam entity, Class<E> boClass) throws BusinessException {
        SysParam sysparam = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(sysparam, boClass);
        return bo;
    }


    @Override
    public List<SysParam> findList(SysParam entity) throws BusinessException {
        return this.sysParamDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(SysParam entity, Class<E> boClass) throws BusinessException {
        List<SysParam> sysparamList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (SysParam sysparam : sysparamList) {
            E bo = BeanCoypUtil.copyToNewObject(sysparam, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(SysParam entity) throws BusinessException {
        return sysParamDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        sysParamDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(SysParam entity) throws BusinessException {
        this.sysParamDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(SysParam entity) throws BusinessException {
        this.sysParamDao.updateOne(entity);
    }

    @Override
    public SysParamBo sysValueByKey(String sysKey) throws BusinessException{
        SysParam search = new SysParam();
        search.setSysKey(sysKey);
        SysParam result =  this.findOne(search);
        if(result == null){
            return null;
        }

        SysParamBo response = new SysParamBo();
        response.setDescription(result.getDescription());
        response.setLanguage(result.getLanguage());
        response.setSysKey(result.getSysKey());
        response.setSysValue(result.getSysValue());

        return response;
    }

}