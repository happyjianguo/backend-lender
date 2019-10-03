package com.yqg.system.service.sysdic.impl;

import com.yqg.api.system.sysdicitem.bo.SysDicItemBo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.system.dao.SysDicDao;
import com.yqg.system.entity.SysDic;
import com.yqg.system.entity.SysDicItem;
import com.yqg.system.service.SysCommonServiceImpl;
import com.yqg.system.service.sysdic.SysDicService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Service("sysDicService")
public class SysDicServiceImpl extends SysCommonServiceImpl implements SysDicService {
    @Autowired
    protected SysDicDao sysDicDao;


    @Override
    public SysDic findById(String id) throws BusinessException {
        return this.sysDicDao.findOneById(id, new SysDic());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        SysDic sysdic = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(sysdic, boClass);
        return bo;
    }

    @Override
    public SysDic findOne(SysDic entity) throws BusinessException {
        return this.sysDicDao.findOne(entity);
    }

    @Override
    public <E> E findOne(SysDic entity, Class<E> boClass) throws BusinessException {
        SysDic sysdic = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(sysdic, boClass);
        return bo;
    }


    @Override
    public List<SysDic> findList(SysDic entity) throws BusinessException {
        return this.sysDicDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(SysDic entity, Class<E> boClass) throws BusinessException {
        List<SysDic> sysdicList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (SysDic sysdic : sysdicList) {
            E bo = BeanCoypUtil.copyToNewObject(sysdic, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(SysDic entity) throws BusinessException {
        return sysDicDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        sysDicDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(SysDic entity) throws BusinessException {
        this.sysDicDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(SysDic entity) throws BusinessException {
        this.sysDicDao.updateOne(entity);
    }

    @Override
    public List<SysDicItem> sysDicItemsListByDicCode(String dicCode) throws BusinessException {
        SysDic dicSearch = new SysDic();
        dicSearch.setDisabled(0);
        dicSearch.setDicCode(dicCode);
        SysDic dicResult = this.findOne(dicSearch);

        String parentId = dicResult.getId();
        List<SysDicItem> dicItemResult = this.sysDicItemService.sysDicItemListByParentId(parentId);

        return dicItemResult;
    }

    public List<SysDicItemBo> dicItemBoListByDicCode(String dicCode) throws BusinessException {
        List<SysDicItem> result = this.sysDicItemsListByDicCode(dicCode);
        List<SysDicItemBo> response = new ArrayList<>();
        for(SysDicItem cell:result){
            SysDicItemBo item = new SysDicItemBo();
            BeanUtils.copyProperties(cell,item);
            response.add(item);
        }
        return response;
    }


}