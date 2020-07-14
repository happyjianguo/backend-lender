package com.yqg.system.service.sysbankbasicinfo.impl;

import com.yqg.api.system.sysbankbasicinfo.bo.SysBankBasicInfoBo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.system.dao.SysBankBasicInfoDao;
import com.yqg.system.entity.SysBankBasicInfo;
import com.yqg.system.service.SysCommonServiceImpl;
import com.yqg.system.service.sysbankbasicinfo.SysBankBasicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 银行基础信息
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Service("sysBankBasicInfoService")
public class SysBankBasicInfoServiceImpl extends SysCommonServiceImpl implements SysBankBasicInfoService {
    @Autowired
    protected SysBankBasicInfoDao sysBankBasicInfoDao;


    @Override
    public SysBankBasicInfo findById(String id) throws BusinessException {
        return this.sysBankBasicInfoDao.findOneById(id, new SysBankBasicInfo());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        SysBankBasicInfo sysbankbasicinfo = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(sysbankbasicinfo, boClass);
        return bo;
    }

    @Override
    public SysBankBasicInfo findOne(SysBankBasicInfo entity) throws BusinessException {
        return this.sysBankBasicInfoDao.findOne(entity);
    }

    @Override
    public <E> E findOne(SysBankBasicInfo entity, Class<E> boClass) throws BusinessException {
        SysBankBasicInfo sysbankbasicinfo = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(sysbankbasicinfo, boClass);
        return bo;
    }


    @Override
    public List<SysBankBasicInfo> findList(SysBankBasicInfo entity) throws BusinessException {
        return this.sysBankBasicInfoDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(SysBankBasicInfo entity, Class<E> boClass) throws BusinessException {
        List<SysBankBasicInfo> sysbankbasicinfoList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (SysBankBasicInfo sysbankbasicinfo : sysbankbasicinfoList) {
            E bo = BeanCoypUtil.copyToNewObject(sysbankbasicinfo, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(SysBankBasicInfo entity) throws BusinessException {
        return sysBankBasicInfoDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        sysBankBasicInfoDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(SysBankBasicInfo entity) throws BusinessException {
        this.sysBankBasicInfoDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(SysBankBasicInfo entity) throws BusinessException {
        this.sysBankBasicInfoDao.updateOne(entity);
    }

    @Override
    public List<SysBankBasicInfoBo> bankList() throws BusinessException{
        SysBankBasicInfo search = new SysBankBasicInfo();
        List<SysBankBasicInfoBo> resposne = this.findList(search,SysBankBasicInfoBo.class);

        return resposne;
    }

    @Override
    public SysBankBasicInfoBo bankInfoByCode(String bankCode) throws BusinessException{
        SysBankBasicInfo search = new SysBankBasicInfo();
        search.setBankCode(bankCode);
        logger.info("SysBankBasicInfoBo bankCode: {}", bankCode);
        return this.findOne(search, SysBankBasicInfoBo.class);
    }

}