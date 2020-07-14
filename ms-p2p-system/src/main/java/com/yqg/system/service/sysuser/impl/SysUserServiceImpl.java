package com.yqg.system.service.sysuser.impl;

import com.yqg.api.system.sysuser.bo.SysUserBo;
import com.yqg.api.system.sysuser.bo.SysUserImageBo;
import com.yqg.api.system.sysuser.bo.SysUserLoginBo;
import com.yqg.api.system.sysuser.ro.*;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.redis.BaseRedisKeyEnums;
import com.yqg.common.utils.*;
import com.yqg.system.dao.SysUserDao;
import com.yqg.system.entity.SysRole;
import com.yqg.system.entity.SysUser;

import com.yqg.system.entity.SysUserRole;
import com.yqg.system.service.SysCommonServiceImpl;
import com.yqg.system.service.sysuser.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 系统用户表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-09-20 15:52:10
 */
@Service("sysUserService")
public class SysUserServiceImpl extends SysCommonServiceImpl implements SysUserService {
    @Autowired
    protected SysUserDao sysuserDao;

    private final static String DEFAULT_PASSWORD = "doitp2p2018";


    @Override
    public SysUser findById(String id) throws BusinessException {
        return this.sysuserDao.findOneById(id, new SysUser());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        SysUser Sysuser = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(Sysuser, boClass);
        return bo;
    }

    @Override
    public SysUser findOne(SysUser entity) throws BusinessException {
        return this.sysuserDao.findOne(entity);
    }

    @Override
    public <E> E findOne(SysUser entity, Class<E> boClass) throws BusinessException {
        SysUser sysuser = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(sysuser, boClass);
        return bo;
    }


    @Override
    public List<SysUser> findList(SysUser entity) throws BusinessException {
        return this.sysuserDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(SysUser entity, Class<E> boClass) throws BusinessException {
        List<SysUser> sysuserList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (SysUser sysuser : sysuserList) {
            E bo = BeanCoypUtil.copyToNewObject(sysuser, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(SysUser entity) throws BusinessException {
        return sysuserDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        sysuserDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(SysUser entity) throws BusinessException {
        this.sysuserDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(SysUser entity) throws BusinessException {
        this.sysuserDao.updateOne(entity);
    }

    @Override
    public BaseResponse<SysUserLoginBo> sysUserLogin(SysUserLoginRo ro) throws BusinessException {
        SysUser search = new SysUser();
        search.setUsername(ro.getUsername());
        search.setPassword(MD5Util.md5LowerCase(ro.getPassword()));
        SysUser result = this.findOne(search);

        if(result == null){
            throw new BusinessException(BaseExceptionEnums.REGEDITER_NOT_ERROR);
        }

        SysUserLoginBo responseData = new SysUserLoginBo();
        String sessionId = UuidUtil.create();
        redisUtil.set(BaseRedisKeyEnums.USER_SESSION_KEY.appendToDefaultKey(sessionId), result.getId());
        responseData.setSessionId(sessionId);
        responseData.setUserId(result.getId());
        responseData.setUsername(result.getUsername());

        BaseResponse<SysUserLoginBo> response = new BaseResponse<SysUserLoginBo>();
        response.setSuccess(true);
        response.setData(responseData);
        if(ro.getPassword().equals(DEFAULT_PASSWORD)){
            response.setCode(BaseExceptionEnums.RESET_YOUR_PASSWORD.getCode());
            response.setMessage(BaseExceptionEnums.RESET_YOUR_PASSWORD.getMessage());
        }
        else{
            response.setCode(BaseResponse.SUCCESS_CODE);
            response.setMessage(BaseResponse.SUCCESS_MESSAGE);
        }

        return response;
    }

    @Transactional
    public SysUser addSysUser(SysUserEditRo ro) throws BusinessException {
        if(StringUtils.isEmpty(ro.getRealname()) ||
                StringUtils.isEmpty(ro.getUsername()) ||
                StringUtils.isEmpty(ro.getRoleIds()) ||
                StringUtils.isEmpty(ro.getRemark()) ||
                //StringUtils.isEmpty(ro.getMobile()) ||
                ro.getThird() == null||
                ro.getStatus() == null){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        SysUser addUser = new SysUser();
        addUser.setDisabled(0);
        addUser.setUsername(ro.getUsername());

        List<SysUser> result = this.findList(addUser);
        if (!CollectionUtils.isEmpty(result)) {
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        //String userPwd = SensitiveInfoUtils.genRandomNum(8);    /*生成随机密码*/
        addUser.setPassword(MD5Util.md5LowerCase(DEFAULT_PASSWORD));
        addUser.setCreateTime(new Date());
        addUser.setStatus(0);
        addUser.setRealname(ro.getRealname());
        addUser.setRemark(ro.getRemark());
        addUser.setThird(ro.getThird());
        addUser.setUpdateTime(new Date());
        addUser.setLastLoginTime(new Date());
        addUser.setMobile(ro.getMobile());
        String userId = this.addOne(addUser);

        this.sysUserRoleService.addUserRoleLink(ro.getRoleIds(),userId);
        //addUser.setPassword(DEFAULT_PASSWORD);
        return addUser;
    }

    /**
     * 修改后台用户*/
    @Transactional
    public SysUser editSysUser(SysUserEditRo ro) throws BusinessException {
        if(StringUtils.isEmpty(ro.getRealname()) ||
                StringUtils.isEmpty(ro.getUsername()) ||
                StringUtils.isEmpty(ro.getRoleIds()) ||
                //StringUtils.isEmpty(ro.getMobile()) ||
                ro.getStatus() == null || ro.getId() == null){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        SysUser editUser = this.findById(ro.getId());
        editUser.setUsername(ro.getUsername());
        editUser.setRealname(ro.getRealname());
        editUser.setStatus(ro.getStatus());
        editUser.setUpdateTime(new Date());
        editUser.setLastLoginTime(new Date());
        editUser.setMobile(ro.getMobile());
        this.updateOne(editUser);

        this.sysUserRoleService.delUserRoleLink(editUser.getId());
        this.sysUserRoleService.addUserRoleLink(ro.getRoleIds(),editUser.getId());
        return editUser;
    }

    /**
     * 查询用户列表*/
    public BasePageResponse<SysUserBo> sysUserList(SysUserSearchRo ro) throws BusinessException {

        SysUser entity = new SysUser();

        if(StringUtils.isNotBlank(ro.getMobile())){
            entity.setMobile(ro.getMobile());
        }
        if(StringUtils.isNotEmpty(ro.getUserName())){
            entity.setUsername(ro.getUserName());
        }

        entity.setDisabled(0);
        Page<SysUser> userPage = this.sysuserDao.findForPage(entity, ro.convertPageRequest());
        BasePageResponse<SysUserBo> response = new BasePageResponse<>(userPage);
        List<SysUser> result = userPage.getContent();

        List<SysUserBo> responseData = new ArrayList<>();


        for(SysUser user:result){
            SysUserBo userCell = new SysUserBo();
            /*通过用户id查出对应的角色id*/
            List<SysUserRole> roleResult = this.sysUserRoleService.userRoleListByUserId(user.getId());
            List<String> roleString = new ArrayList<>();
            for(SysUserRole item:roleResult){        /*通过角色id查出角色名称*/
                String roleId = item.getRoleId();
                SysRole roleName = this.sysRoleService.findById(roleId);
                if(roleName != null){
                    String temp = String.valueOf(roleId)+"|"+roleName.getRoleName();
                    roleString.add(temp);
                }
            }
            userCell.setCreateTime(user.getCreateTime());
            userCell.setId(user.getId());
            userCell.setRealname(user.getRealname());
            userCell.setUpdateTime(user.getUpdateTime());
            userCell.setUsername(user.getUsername());
            userCell.setThird(user.getThird());
            userCell.setRoles(StringUtils.join(roleString.toArray(),","));
            userCell.setStatus(user.getStatus());
            userCell.setMobile(user.getMobile());
            responseData.add(userCell);
        }
        response.setContent(responseData);

        return response;

    }

    @Override
    public SysUserImageBo lookUserImage(SysUserImageRo ro) throws BusinessException {

        byte[] bytes = FileUtils.file2Byte(ro.getUserImageUrl());
        logger.info("用户图片url转换为byte:{}",bytes);
        if(bytes!=null){
            SysUserImageBo bo = new SysUserImageBo();
            String encode = Base64Utils.encode(bytes);
            bo.setImgBase64(encode);
            logger.info("用户图片url转换为encode:{}",encode);
            return bo;
        }else {
            logger.info("转换byte失败：{}",bytes);
            throw new BusinessException(BaseExceptionEnums.SYSTEEM_MAINTAIN_ERROR);
        }

    }

    @Override
    @Transactional
    public void editSysUserPassword(SysUserChangePasswdRo ro) throws BusinessException{
        SysUser search = new SysUser();
        search.setId(ro.getUserId());
        search.setPassword(MD5Util.md5LowerCase(ro.getOldPassword()));
        SysUser result = this.findOne(search);

        if(result == null){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }

        result.setPassword(MD5Util.md5LowerCase(ro.getNewPassword()));
        result.setUpdateTime(new Date());
        this.updateOne(result);

    }
    @Override
    @Transactional
    public void resetSysUserPassword(BaseSessionIdRo ro) throws BusinessException{
        SysUser search = new SysUser();
        search.setId(ro.getUserId());
        SysUser result = this.findOne(search);

        if(result == null){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }

        result.setPassword(MD5Util.md5LowerCase(DEFAULT_PASSWORD));
        this.updateOne(result);
    }
}