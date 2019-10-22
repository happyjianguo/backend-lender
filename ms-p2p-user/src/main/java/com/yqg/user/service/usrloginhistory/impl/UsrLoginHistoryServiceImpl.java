package com.yqg.user.service.usrloginhistory.impl;

import com.yqg.api.user.usrloginhistory.ro.UsrLoginHistoryRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.user.dao.UsrLoginHistoryDao;
import com.yqg.user.entity.UsrLoginHistory;
import com.yqg.user.service.UserCommonServiceImpl;
import com.yqg.user.service.usrloginhistory.UsrLoginHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户登录历史表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Service("usrLoginHistoryService")
public class UsrLoginHistoryServiceImpl extends UserCommonServiceImpl implements UsrLoginHistoryService {
    @Autowired
    protected UsrLoginHistoryDao usrloginhistoryDao;


    @Override
    public UsrLoginHistory findById(String id) throws BusinessException {
        return this.usrloginhistoryDao.findOneById(id, new UsrLoginHistory());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        UsrLoginHistory usrloginhistory = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(usrloginhistory, boClass);
        return bo;
    }

    @Override
    public UsrLoginHistory findOne(UsrLoginHistory entity) throws BusinessException {
        return this.usrloginhistoryDao.findOne(entity);
    }

    @Override
    public <E> E findOne(UsrLoginHistory entity, Class<E> boClass) throws BusinessException {
        UsrLoginHistory usrloginhistory = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(usrloginhistory, boClass);
        return bo;
    }


    @Override
    public List<UsrLoginHistory> findList(UsrLoginHistory entity) throws BusinessException {
        return this.usrloginhistoryDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(UsrLoginHistory entity, Class<E> boClass) throws BusinessException {
        List<UsrLoginHistory> usrloginhistoryList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (UsrLoginHistory usrloginhistory : usrloginhistoryList) {
            E bo = BeanCoypUtil.copyToNewObject(usrloginhistory, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(UsrLoginHistory entity) throws BusinessException {
        return usrloginhistoryDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        usrloginhistoryDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(UsrLoginHistory entity) throws BusinessException {
        this.usrloginhistoryDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(UsrLoginHistory entity) throws BusinessException {
        this.usrloginhistoryDao.updateOne(entity);
    }


    @Override
    @Transactional
    public void addPcLoginHistory(String ipAddress, String userId) throws BusinessException {
        UsrLoginHistory addInfo = new UsrLoginHistory();
        addInfo.setIpAddress(ipAddress);
        addInfo.setDeviceType("PC");
        addInfo.setNetworkType("1");
        addInfo.setUserUuid(userId);
        this.addOne(addInfo);
    }

    public void addLoginHistory(UsrLoginHistoryRo ro) throws BusinessException {

    }
}