package com.yqg.system.service.sysoperatehistory.impl;

import com.yqg.api.system.sysoperatehistory.ro.SysOperateHistoryAddRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.system.dao.SysOperateHistoryDao;
import com.yqg.system.entity.SysOperateHistory;
import com.yqg.system.service.SysCommonServiceImpl;
import com.yqg.system.service.sysoperatehistory.SysOperateHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("sysOperateHistoryService")
public class SysOperateHistoryServiceImpl extends SysCommonServiceImpl implements SysOperateHistoryService {
    @Autowired
    protected SysOperateHistoryDao sysOperateHistoryDao;

    @Override
    public SysOperateHistory findById(String id) throws BusinessException {
        return this.sysOperateHistoryDao.findOneById(id, new SysOperateHistory());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        SysOperateHistory sysOperateHistory = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(sysOperateHistory, boClass);
        return bo;
    }

    @Override
    public SysOperateHistory findOne(SysOperateHistory entity) throws BusinessException {
        return this.sysOperateHistoryDao.findOne(entity);
    }

    @Override
    public <E> E findOne(SysOperateHistory entity, Class<E> boClass) throws BusinessException {
        SysOperateHistory sysOperateHistory = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(sysOperateHistory, boClass);
        return bo;
    }


    @Override
    public List<SysOperateHistory> findList(SysOperateHistory entity) throws BusinessException {
        return this.sysOperateHistoryDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(SysOperateHistory entity, Class<E> boClass) throws BusinessException {
        List<SysOperateHistory> sysOperateHistory = findList(entity);
        List<E> boList = new ArrayList<>();
        for (SysOperateHistory studentloanstepfour : sysOperateHistory) {
            E bo = BeanCoypUtil.copyToNewObject(studentloanstepfour, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(SysOperateHistory entity) throws BusinessException {
        return sysOperateHistoryDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        sysOperateHistoryDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(SysOperateHistory entity) throws BusinessException {
        this.sysOperateHistoryDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(SysOperateHistory entity) throws BusinessException {
        this.sysOperateHistoryDao.updateOne(entity);
    }

    @Override
    @Transactional
    public void addOperateHistory(SysOperateHistoryAddRo ro) throws BusinessException{
        SysOperateHistory addInfo = new SysOperateHistory();
        addInfo.setIpAddress(ro.getIpAddress());
        addInfo.setOperateString(ro.getOperateString());
        addInfo.setType(ro.getType());
        addInfo.setCreateUser(ro.getUserId());
        this.addOne(addInfo);
    }


}
