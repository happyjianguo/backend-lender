package com.yqg.user.service.useraddressdetail.impl;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.user.dao.UserAddressDetailDao;
import com.yqg.user.entity.UserAddressDetail;
import com.yqg.user.service.UserCommonServiceImpl;
import com.yqg.user.service.useraddressdetail.UserAddressDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户地址信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Service("userAddressDetailService")
public class UserAddressDetailServiceImpl extends UserCommonServiceImpl implements UserAddressDetailService {
    @Autowired
    protected UserAddressDetailDao userAddressDetailDao;


    @Override
    public UserAddressDetail findById(String id) throws BusinessException {
        return this.userAddressDetailDao.findOneById(id, new UserAddressDetail());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        UserAddressDetail useraddressdetail = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(useraddressdetail, boClass);
        return bo;
    }

    @Override
    public UserAddressDetail findOne(UserAddressDetail entity) throws BusinessException {
        return this.userAddressDetailDao.findOne(entity);
    }

    @Override
    public <E> E findOne(UserAddressDetail entity, Class<E> boClass) throws BusinessException {
        UserAddressDetail useraddressdetail = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(useraddressdetail, boClass);
        return bo;
    }


    @Override
    public List<UserAddressDetail> findList(UserAddressDetail entity) throws BusinessException {
        return this.userAddressDetailDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(UserAddressDetail entity, Class<E> boClass) throws BusinessException {
        List<UserAddressDetail> useraddressdetailList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (UserAddressDetail useraddressdetail : useraddressdetailList) {
            E bo = BeanCoypUtil.copyToNewObject(useraddressdetail, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(UserAddressDetail entity) throws BusinessException {
        return userAddressDetailDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        userAddressDetailDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(UserAddressDetail entity) throws BusinessException {
        this.userAddressDetailDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(UserAddressDetail entity) throws BusinessException {
        this.userAddressDetailDao.updateOne(entity);
    }

    @Override
    public void addInfo(UserAddressDetail addInfo) throws BusinessException {
        UserAddressDetail search = new UserAddressDetail();
        search.setUserUuid(addInfo.getUserUuid());
        search.setAddressType(addInfo.getAddressType());
        List<UserAddressDetail> result = this.findList(search);
        for(UserAddressDetail cell:result){
            this.deleteOne(cell);
        }
        this.addOne(addInfo);
    }

}