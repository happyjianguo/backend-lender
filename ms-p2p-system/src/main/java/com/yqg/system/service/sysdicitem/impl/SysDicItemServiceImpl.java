package com.yqg.system.service.sysdicitem.impl;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.system.dao.SysDicItemDao;
import com.yqg.system.entity.SysDicItem;
import com.yqg.system.service.SysCommonServiceImpl;
import com.yqg.system.service.sysdicitem.SysDicItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典项表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Service("sysDicItemService")
public class SysDicItemServiceImpl extends SysCommonServiceImpl implements SysDicItemService {
    @Autowired
    protected SysDicItemDao sysDicItemDao;


    @Override
    public SysDicItem findById(String id) throws BusinessException {
        return this.sysDicItemDao.findOneById(id, new SysDicItem());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        SysDicItem sysdicitem = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(sysdicitem, boClass);
        return bo;
    }

    @Override
    public SysDicItem findOne(SysDicItem entity) throws BusinessException {
        return this.sysDicItemDao.findOne(entity);
    }

    @Override
    public <E> E findOne(SysDicItem entity, Class<E> boClass) throws BusinessException {
        SysDicItem sysdicitem = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(sysdicitem, boClass);
        return bo;
    }


    @Override
    public List<SysDicItem> findList(SysDicItem entity) throws BusinessException {
        return this.sysDicItemDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(SysDicItem entity, Class<E> boClass) throws BusinessException {
        List<SysDicItem> sysdicitemList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (SysDicItem sysdicitem : sysdicitemList) {
            E bo = BeanCoypUtil.copyToNewObject(sysdicitem, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(SysDicItem entity) throws BusinessException {
        return sysDicItemDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        sysDicItemDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(SysDicItem entity) throws BusinessException {
        this.sysDicItemDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(SysDicItem entity) throws BusinessException {
        this.sysDicItemDao.updateOne(entity);
    }

    @Override
    public List<SysDicItem> sysDicItemListByParentId(String parentId) throws BusinessException {
        SysDicItem search = new SysDicItem();
        search.setDisabled(0);
        search.setDicId(parentId);
        return this.findList(search);
    }

}