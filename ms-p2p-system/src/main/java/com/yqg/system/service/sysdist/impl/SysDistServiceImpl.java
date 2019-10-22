package com.yqg.system.service.sysdist.impl;

import com.yqg.api.system.sysdist.bo.SysDistBo;
import com.yqg.api.system.sysdist.ro.SysDistRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.system.dao.SysDistDao;
import com.yqg.system.entity.SysDist;
import com.yqg.system.service.SysCommonServiceImpl;
import com.yqg.system.service.sysdist.SysDistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 行政区划表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Service("sysDistService")
public class SysDistServiceImpl extends SysCommonServiceImpl implements SysDistService {
    @Autowired
    protected SysDistDao sysDistDao;


    @Override
    public SysDist findById(String id) throws BusinessException {
        return this.sysDistDao.findOneById(id, new SysDist());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        SysDist sysdist = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(sysdist, boClass);
        return bo;
    }

    @Override
    public SysDist findOne(SysDist entity) throws BusinessException {
        return this.sysDistDao.findOne(entity);
    }

    @Override
    public <E> E findOne(SysDist entity, Class<E> boClass) throws BusinessException {
        SysDist sysdist = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(sysdist, boClass);
        return bo;
    }


    @Override
    public List<SysDist> findList(SysDist entity) throws BusinessException {
        return this.sysDistDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(SysDist entity, Class<E> boClass) throws BusinessException {
        List<SysDist> sysdistList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (SysDist sysdist : sysdistList) {
            E bo = BeanCoypUtil.copyToNewObject(sysdist, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(SysDist entity) throws BusinessException {
        return sysDistDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        sysDistDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(SysDist entity) throws BusinessException {
        this.sysDistDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(SysDist entity) throws BusinessException {
        this.sysDistDao.updateOne(entity);
    }

    public List<SysDistBo> getDistList(SysDistRo ro) throws BusinessException {
        String parentCode = ro.getParentCode();
        String distLevel = ro.getDistLevel();

        SysDist search = new SysDist();
        search.setParentCode(parentCode);
        search.setDistLevel(distLevel);
        List<SysDistBo> result = this.findList(search, SysDistBo.class);

        return result;
    }
}