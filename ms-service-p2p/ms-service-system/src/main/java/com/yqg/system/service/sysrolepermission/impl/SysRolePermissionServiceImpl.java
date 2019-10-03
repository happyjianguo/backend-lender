package com.yqg.system.service.sysrolepermission.impl;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.system.dao.SysRolePermissionDao;
import com.yqg.system.entity.SysRolePermission;
import com.yqg.system.service.SysCommonServiceImpl;
import com.yqg.system.service.sysrolepermission.SysRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统角色权限表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@Service("sysRolePermissionService")
public class SysRolePermissionServiceImpl extends SysCommonServiceImpl implements SysRolePermissionService {
    @Autowired
    protected SysRolePermissionDao sysrolepermissionDao;


    @Override
    public SysRolePermission findById(String id) throws BusinessException {
        return this.sysrolepermissionDao.findOneById(id, new SysRolePermission());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        SysRolePermission sysrolepermission = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(sysrolepermission, boClass);
        return bo;
    }

    @Override
    public SysRolePermission findOne(SysRolePermission entity) throws BusinessException {
        return this.sysrolepermissionDao.findOne(entity);
    }

    @Override
    public <E> E findOne(SysRolePermission entity, Class<E> boClass) throws BusinessException {
        SysRolePermission sysrolepermission = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(sysrolepermission, boClass);
        return bo;
    }


    @Override
    public List<SysRolePermission> findList(SysRolePermission entity) throws BusinessException {
        return this.sysrolepermissionDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(SysRolePermission entity, Class<E> boClass) throws BusinessException {
        List<SysRolePermission> sysrolepermissionList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (SysRolePermission sysrolepermission : sysrolepermissionList) {
            E bo = BeanCoypUtil.copyToNewObject(sysrolepermission, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(SysRolePermission entity) throws BusinessException {
        return sysrolepermissionDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        sysrolepermissionDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(SysRolePermission entity) throws BusinessException {
        this.sysrolepermissionDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(SysRolePermission entity) throws BusinessException {
        this.sysrolepermissionDao.updateOne(entity);
    }


    public List<SysRolePermission> SysRolePermissItemByRoleId (String roleId) throws BusinessException {
        SysRolePermission search = new SysRolePermission();
        search.setRoleId(roleId);
        search.setDisabled(0);
        List<SysRolePermission> result = this.findList(search);
        return result;
    }

    @Transactional
    public void delSysRolePermissionItemByRoleId(String roleId) throws BusinessException{
        SysRolePermission search = new SysRolePermission();
        search.setRoleId(roleId);
        List<SysRolePermission> result = this.findList(search);

        for(SysRolePermission cell:result){
            this.deleteById(cell.getId());
        }
    }

}