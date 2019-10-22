package com.yqg.system.service.sysuserrole.impl;

import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.system.dao.SysUserRoleDao;
import com.yqg.system.entity.SysUserRole;
import com.yqg.system.service.SysCommonServiceImpl;
import com.yqg.system.service.sysuserrole.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户角色中间表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends SysCommonServiceImpl implements SysUserRoleService {
    @Autowired
    protected SysUserRoleDao sysuserroleDao;


    @Override
    public SysUserRole findById(String id) throws BusinessException {
        return this.sysuserroleDao.findOneById(id, new SysUserRole());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        SysUserRole sysuserrole = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(sysuserrole, boClass);
        return bo;
    }

    @Override
    public SysUserRole findOne(SysUserRole entity) throws BusinessException {
        return this.sysuserroleDao.findOne(entity);
    }

    @Override
    public <E> E findOne(SysUserRole entity, Class<E> boClass) throws BusinessException {
        SysUserRole sysuserrole = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(sysuserrole, boClass);
        return bo;
    }


    @Override
    public List<SysUserRole> findList(SysUserRole entity) throws BusinessException {
        return this.sysuserroleDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(SysUserRole entity, Class<E> boClass) throws BusinessException {
        List<SysUserRole> sysuserroleList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (SysUserRole sysuserrole : sysuserroleList) {
            E bo = BeanCoypUtil.copyToNewObject(sysuserrole, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(SysUserRole entity) throws BusinessException {
        return sysuserroleDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        sysuserroleDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(SysUserRole entity) throws BusinessException {
        this.sysuserroleDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(SysUserRole entity) throws BusinessException {
        this.sysuserroleDao.updateOne(entity);
    }

    /**
     * 新增用户与角色关联关系*/
    @Transactional
    public void addUserRoleLink(String roleIds,String userId) throws BusinessException {
        if(StringUtils.isEmpty(roleIds) || userId == null){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        String[] roleIdString = roleIds.split(",");
        for(String roleItem:roleIdString){
            SysUserRole info = new SysUserRole();
            info.setRoleId(roleItem);
            info.setUserId(userId);
            info.setStatus(0);
            this.addOne(info);
        }

    }

    /**
     * 删除用户与角色关联关系*/
    @Transactional
    public void delUserRoleLink(String userId) throws BusinessException {
        SysUserRole search = new SysUserRole();
        search.setUserId(userId);
        List<SysUserRole> result = this.findList(search);

        for(SysUserRole cell:result){
            this.deleteById(cell.getId());
        }
    }

    /**
     * 通过用户的id查询关联的角色*/
    public List<SysUserRole> userRoleListByUserId(String userId) throws BusinessException {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);
        return this.findList(sysUserRole);
    }

}