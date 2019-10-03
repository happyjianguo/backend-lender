package com.yqg.system.service.syspermission.impl;

import com.yqg.api.system.syspermission.bo.SysPermissionListBo;
import com.yqg.api.system.syspermission.ro.SysPermissionRo;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.system.dao.SysPermissionDao;
import com.yqg.system.entity.SysPermission;
import com.yqg.system.entity.SysRolePermission;
import com.yqg.system.entity.SysUserRole;
import com.yqg.system.service.SysCommonServiceImpl;
import com.yqg.system.service.syspermission.SysPermissionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统权限表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@Service("SysPermissionService")
public class SysPermissionServiceImpl extends SysCommonServiceImpl implements SysPermissionService {
    @Autowired
    protected SysPermissionDao syspermissionDao;


    @Override
    public SysPermission findById(String id) throws BusinessException {
        return this.syspermissionDao.findOneById(id, new SysPermission());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        SysPermission syspermission = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(syspermission, boClass);
        return bo;
    }

    @Override
    public SysPermission findOne(SysPermission entity) throws BusinessException {
        return this.syspermissionDao.findOne(entity);
    }

    @Override
    public <E> E findOne(SysPermission entity, Class<E> boClass) throws BusinessException {
        SysPermission syspermission = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(syspermission, boClass);
        return bo;
    }


    @Override
    public List<SysPermission> findList(SysPermission entity) throws BusinessException {
        return this.syspermissionDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(SysPermission entity, Class<E> boClass) throws BusinessException {
        List<SysPermission> syspermissionList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (SysPermission syspermission : syspermissionList) {
            E bo = BeanCoypUtil.copyToNewObject(syspermission, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(SysPermission entity) throws BusinessException {
        return syspermissionDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        syspermissionDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(SysPermission entity) throws BusinessException {
        this.syspermissionDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(SysPermission entity) throws BusinessException {
        this.syspermissionDao.updateOne(entity);
    }

    @Override
    @Transactional
    public void addPermissionItem(SysPermissionRo ro) throws BusinessException {
        if(ro.getParentId() == null){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }

        SysPermission addInfo = this.getRequestData(ro);
        addInfo.setParentId(ro.getParentId());
        this.addOne(addInfo);
    }

    @Override
    @Transactional
    public void permissionItemEdit(SysPermissionRo ro) throws BusinessException {
        if(StringUtils.isEmpty(ro.getId())){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        this.getRequestData(ro);
        SysPermission result = this.findById(ro.getId());
        result.setPermissionUrl(ro.getPermissionUrl());
        result.setPermissionName(ro.getPermissionName());
        result.setPermissionCode(ro.getPermissionCode());
        this.updateOne(result);
    }

    @Transactional
    public SysPermission getRequestData(SysPermissionRo ro) throws BusinessException {
        if(StringUtils.isEmpty(ro.getPermissionCode()) || StringUtils.isEmpty(ro.getPermissionName()) || StringUtils.isEmpty(ro.getPermissionUrl())){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        SysPermission permission = new SysPermission();
        permission.setPermissionCode(ro.getPermissionCode());
        permission.setPermissionName(ro.getPermissionName());
        permission.setPermissionUrl(ro.getPermissionUrl());
        return permission;
    }

    @Override
    public List<SysPermissionListBo> permissionList(List<String> permissionsIds) throws BusinessException {
        List<SysPermissionListBo> response = new ArrayList<>();
        SysPermission parentSearch = new SysPermission();
        parentSearch.setParentId("0");
        List<SysPermission> parent = this.findList(parentSearch);

        if(CollectionUtils.isEmpty(parent)){
            return null;
        }

        /*遍历父节点*/
        for(SysPermission item:parent){
            SysPermissionListBo temp = new SysPermissionListBo();
            temp.setPermissionCode(item.getPermissionCode());
            temp.setPermissionName(item.getPermissionName());
            temp.setPermissionUrl(item.getPermissionUrl());
            temp.setId(item.getId());

            if(permissionsIds.contains(item.getId())){
                temp.setIsCheck(true);
            }else {
                temp.setIsCheck(false);
            }

            SysPermission parentInfo = new SysPermission();
            parentInfo.setParentId(item.getId());
            /*根据父节点查询子节点*/
            List<SysPermission> childrenResult = this.findList(parentInfo);
            List<SysPermissionListBo> childrenList = new ArrayList<>();

            /*遍历子节点*/
            for (SysPermission obj:childrenResult){
                SysPermissionListBo tempObj = new SysPermissionListBo();
                tempObj.setPermissionCode(obj.getPermissionCode());
                tempObj.setPermissionName(obj.getPermissionName());
                tempObj.setPermissionUrl(obj.getPermissionUrl());
                tempObj.setId(obj.getId());
                if(permissionsIds.contains(obj.getId())){
                    tempObj.setIsCheck(true);
                }else {
                    tempObj.setIsCheck(false);
                }
                childrenList.add(tempObj);
            }
            temp.setChildren(childrenList);
            response.add(temp);

        }

        return response;
    }

    /**
     * 通过用户id查询用有的权限*/
    public List<SysPermissionListBo> permissionTreeByUserId(String userId) throws BusinessException {
        if(StringUtils.isEmpty(userId)){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        List<SysPermissionListBo> responses = new ArrayList<>();
        //根据用户查询所拥有的角色
        List<SysUserRole> userRoles = this.sysUserRoleService.userRoleListByUserId(userId);

        if(userRoles != null && userRoles.size() > 0){
            List<SysRolePermission> allPermissionList = new ArrayList<>();
            //根据角色ID可以获取角色ID下的权限列表
            for(SysUserRole roleItem:userRoles){
                List<SysRolePermission> tempPermissionList = this.sysRolePermissionService.SysRolePermissItemByRoleId(roleItem.getRoleId());
                allPermissionList.addAll(tempPermissionList);
            }
            if(allPermissionList != null && allPermissionList.size() > 0){
                /*所有权限id*/
                Set<String> permissionIdList = new HashSet<>(allPermissionList.size());
                permissionIdList.addAll(allPermissionList.stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList()));

                List<SysPermission> permissionList = this.getPermissionListById(permissionIdList);

                /*所有顶级菜单*/
                List<SysPermission> topPermissions = new ArrayList<>();
                topPermissions.addAll(permissionList.stream().filter(sysPermission -> sysPermission.getParentId().equals("0")).distinct().collect(Collectors.toList()));
                permissionList.removeAll(topPermissions);   /*去除顶级菜单,*/
                for(SysPermission permission : topPermissions){
                    SysPermissionListBo top = this.getPermissionRes(permission);
                    List<SysPermissionListBo> childList = new ArrayList<>();
                    for(SysPermission childPermission : permissionList){
                        if(childPermission.getParentId().equals(permission.getId())){
                            SysPermissionListBo child = this.getPermissionRes(childPermission);

                            childList.add(child);
                        }
                        top.setChildren(childList);
                    }
                    responses.add(top);
                }
            }
        }
        return responses;
    }

    /**
     * 通过id查询权限列表*/
    public List<SysPermission> getPermissionListById(Set<String> permissionIdList) throws BusinessException {
        List<SysPermission> result = new ArrayList<>();
        for(String cell:permissionIdList){
            result.add(this.findById(cell));
        }
        return result;
    }

    /**
     * 返回权限子项初始化数据*/
    private SysPermissionListBo getPermissionRes(SysPermission permission)
            throws BusinessException {
        SysPermissionListBo top = new SysPermissionListBo();
        top.setId(permission.getId());
        top.setPermissionUrl(permission.getPermissionUrl());
        top.setPermissionName(permission.getPermissionName());
        top.setPermissionCode(permission.getPermissionCode());
        return top;
    }
}