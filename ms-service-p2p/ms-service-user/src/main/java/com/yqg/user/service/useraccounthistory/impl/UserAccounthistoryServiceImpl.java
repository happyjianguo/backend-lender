package com.yqg.user.service.useraccounthistory.impl;

import com.yqg.api.user.useraccounthistory.ro.UserAccounthistoryRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.user.dao.UserAccounthistoryDao;
import com.yqg.user.entity.UserAccounthistory;
import com.yqg.user.service.UserAccounthistoryCommonServiceImpl;
import com.yqg.user.service.useraccounthistory.UserAccounthistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户账户明细表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-11 10:43:36
 */
@Service("userAccountHistoryService")
public class UserAccounthistoryServiceImpl extends UserAccounthistoryCommonServiceImpl implements UserAccounthistoryService {
    @Autowired
    protected UserAccounthistoryDao useraccounthistoryDao;


    @Override
    public UserAccounthistory findById(String id) throws BusinessException {
        return this.useraccounthistoryDao.findOneById(id, new UserAccounthistory());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        UserAccounthistory useraccounthistory = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(useraccounthistory, boClass);
        return bo;
    }

    @Override
    public UserAccounthistory findOne(UserAccounthistory entity) throws BusinessException {
        return this.useraccounthistoryDao.findOne(entity);
    }

    @Override
    public <E> E findOne(UserAccounthistory entity, Class<E> boClass) throws BusinessException {
        UserAccounthistory useraccounthistory = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(useraccounthistory, boClass);
        return bo;
    }


    @Override
    public List<UserAccounthistory> findList(UserAccounthistory entity) throws BusinessException {
        return this.useraccounthistoryDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(UserAccounthistory entity, Class<E> boClass) throws BusinessException {
        List<UserAccounthistory> useraccounthistoryList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (UserAccounthistory useraccounthistory : useraccounthistoryList) {
            E bo = BeanCoypUtil.copyToNewObject(useraccounthistory, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(UserAccounthistory entity) throws BusinessException {
        return useraccounthistoryDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        useraccounthistoryDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(UserAccounthistory entity) throws BusinessException {
        this.useraccounthistoryDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(UserAccounthistory entity) throws BusinessException {
        this.useraccounthistoryDao.updateOne(entity);
    }

    /**
     * 添加用户账户明细
     * */
    @Override
    public void insterUserAccountHistory(UserAccounthistoryRo userAccounthistoryRo) throws BusinessException {
        UserAccounthistory entity = new UserAccounthistory();
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setType(userAccounthistoryRo.getType());
        entity.setProductType(userAccounthistoryRo.getProductType());
        entity.setUserUuid(userAccounthistoryRo.getUserUuid());
        entity.setTradeBalance(userAccounthistoryRo.getTradeBalance());
        this.addOne(entity);
    }
}