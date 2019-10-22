package com.yqg.system.service.sysrole.impl;

import com.yqg.api.system.syspermission.bo.SysPermissionListBo;
import com.yqg.api.system.sysrole.ro.ManSysRoleRo;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.system.dao.SysRoleDao;
import com.yqg.system.entity.SysRole;
import com.yqg.system.entity.SysRolePermission;
import com.yqg.system.service.SysCommonServiceImpl;
import com.yqg.system.service.sysrole.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统角色表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends SysCommonServiceImpl implements SysRoleService {
    @Autowired
    protected SysRoleDao sysroleDao;


    @Override
    public SysRole findById(String id) throws BusinessException {
        return this.sysroleDao.findOneById(id, new SysRole());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        SysRole sysrole = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(sysrole, boClass);
        return bo;
    }

    @Override
    public SysRole findOne(SysRole entity) throws BusinessException {
        return this.sysroleDao.findOne(entity);
    }

    @Override
    public <E> E findOne(SysRole entity, Class<E> boClass) throws BusinessException {
        SysRole sysrole = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(sysrole, boClass);
        return bo;
    }


    @Override
    public List<SysRole> findList(SysRole entity) throws BusinessException {
        return this.sysroleDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(SysRole entity, Class<E> boClass) throws BusinessException {
        List<SysRole> sysroleList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (SysRole sysrole : sysroleList) {
            E bo = BeanCoypUtil.copyToNewObject(sysrole, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(SysRole entity) throws BusinessException {
        return sysroleDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        sysroleDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(SysRole entity) throws BusinessException {
        this.sysroleDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(SysRole entity) throws BusinessException {
        this.sysroleDao.updateOne(entity);
    }

    @Override
    public List<SysRole> sysRolesList() throws BusinessException {

        List<SysRole> result = this.findList(new SysRole());

        return result;
    }

    @Override
    @Transactional
    public void addSysRole(ManSysRoleRo ro) throws BusinessException {
        SysRole addInfo = this.getCommonInfo(ro);
        String roleId = this.addOne(addInfo);

        String[] permissionString = ro.getPermissionIds().split(",");
        this.addRolePermissionLink(permissionString,roleId);
    }

    @Override
    public List<SysPermissionListBo> rolePermissionCheckList(ManSysRoleRo request)
            throws BusinessException{
        if(StringUtils.isEmpty(request.getId())){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        List<String> permissionsIds = new ArrayList<>();

        List<SysRolePermission> permissionsList = this.sysRolePermissionService.SysRolePermissItemByRoleId(request.getId());
        for(SysRolePermission item:permissionsList){
            permissionsIds.add(item.getPermissionId());
        }
        List<SysPermissionListBo> permissionsResult = this.sysPermissionService.permissionList(permissionsIds);

        return permissionsResult;
    }

    /**
     * 修改系统角色*/
    @Transactional
    public void sysRoleEdit(ManSysRoleRo ro) throws BusinessException{
        if(ro.getId() == null){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }


        SysRole searchResult = this.findById(ro.getId());
        searchResult.setStatus(ro.getStatus());
        searchResult.setRoleName(ro.getRoleName());


        this.sysRolePermissionService.delSysRolePermissionItemByRoleId(ro.getId());

        String[] permissionString = ro.getPermissionIds().split(",");
        this.addRolePermissionLink(permissionString, ro.getId());
    }


    public SysRole getCommonInfo(ManSysRoleRo ro) throws BusinessException {
        if(StringUtils.isEmpty(ro.getPermissionIds()) || StringUtils.isEmpty(ro.getRoleName()) ||
                StringUtils.isEmpty(ro.getRemark())){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        SysRole info = new SysRole();
        info.setRoleName(ro.getRoleName());
        info.setRemark(ro.getRemark());
        info.setStatus(ro.getStatus());
        return info;
    }

    /**
     * 添加系统角色权限关联关系*/
    public void addRolePermissionLink(String[] permissionString,String roleId) throws BusinessException {
        for(String cell:permissionString){
            SysRolePermission addInfo = new SysRolePermission();
            addInfo.setRoleId(roleId);
            addInfo.setPermissionId(cell);
            this.sysRolePermissionService.addOne(addInfo);
        }
    }
}