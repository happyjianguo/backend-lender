package com.yqg.system.service.syspaymentchannel.impl;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.system.dao.SysPaymentChannelDao;
import com.yqg.system.entity.SysPaymentChannel;
import com.yqg.system.service.SysCommonServiceImpl;
import com.yqg.system.service.syspaymentchannel.SysPaymentChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Service("sysPaymentChannelService")
public class SysPaymentChannelServiceImpl extends SysCommonServiceImpl implements SysPaymentChannelService {
    @Autowired
    protected SysPaymentChannelDao sysPaymentChannelDao;


    @Override
    public SysPaymentChannel findById(String id) throws BusinessException {
        return this.sysPaymentChannelDao.findOneById(id, new SysPaymentChannel());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        SysPaymentChannel syspaymentchannel = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(syspaymentchannel, boClass);
        return bo;
    }

    @Override
    public SysPaymentChannel findOne(SysPaymentChannel entity) throws BusinessException {
        return this.sysPaymentChannelDao.findOne(entity);
    }

    @Override
    public <E> E findOne(SysPaymentChannel entity, Class<E> boClass) throws BusinessException {
        SysPaymentChannel syspaymentchannel = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(syspaymentchannel, boClass);
        return bo;
    }


    @Override
    public List<SysPaymentChannel> findList(SysPaymentChannel entity) throws BusinessException {
        return this.sysPaymentChannelDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(SysPaymentChannel entity, Class<E> boClass) throws BusinessException {
        List<SysPaymentChannel> syspaymentchannelList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (SysPaymentChannel syspaymentchannel : syspaymentchannelList) {
            E bo = BeanCoypUtil.copyToNewObject(syspaymentchannel, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(SysPaymentChannel entity) throws BusinessException {
        return sysPaymentChannelDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        sysPaymentChannelDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(SysPaymentChannel entity) throws BusinessException {
        this.sysPaymentChannelDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(SysPaymentChannel entity) throws BusinessException {
        this.sysPaymentChannelDao.updateOne(entity);
    }

}