package com.yqg.system.service.systhirdlogs.impl;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.system.dao.SysThirdLogsDao;
import com.yqg.system.entity.SysThirdLogs;
import com.yqg.system.service.SysCommonServiceImpl;
import com.yqg.system.service.systhirdlogs.SysThirdLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 第三方日志信息
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Service("sysThirdLogsService")
public class SysThirdLogsServiceImpl extends SysCommonServiceImpl implements SysThirdLogsService {
    @Autowired
    protected SysThirdLogsDao sysThirdLogsDao;


    @Override
    public SysThirdLogs findById(String id) throws BusinessException {
        return this.sysThirdLogsDao.findOneById(id, new SysThirdLogs());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        SysThirdLogs systhirdlogs = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(systhirdlogs, boClass);
        return bo;
    }

    @Override
    public SysThirdLogs findOne(SysThirdLogs entity) throws BusinessException {
        return this.sysThirdLogsDao.findOne(entity);
    }

    @Override
    public <E> E findOne(SysThirdLogs entity, Class<E> boClass) throws BusinessException {
        SysThirdLogs systhirdlogs = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(systhirdlogs, boClass);
        return bo;
    }


    @Override
    public List<SysThirdLogs> findList(SysThirdLogs entity) throws BusinessException {
        return this.sysThirdLogsDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(SysThirdLogs entity, Class<E> boClass) throws BusinessException {
        List<SysThirdLogs> systhirdlogsList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (SysThirdLogs systhirdlogs : systhirdlogsList) {
            E bo = BeanCoypUtil.copyToNewObject(systhirdlogs, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(SysThirdLogs entity) throws BusinessException {
        return sysThirdLogsDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        sysThirdLogsDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(SysThirdLogs entity) throws BusinessException {
        this.sysThirdLogsDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(SysThirdLogs entity) throws BusinessException {
        this.sysThirdLogsDao.updateOne(entity);
    }

}