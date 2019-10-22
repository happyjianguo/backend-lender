package com.yqg.system.service.sysuserrole;

import com.yqg.common.dynamicdata.TargetDataSource;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.core.BaseService;
import com.yqg.system.entity.SysUserRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户角色中间表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
public interface SysUserRoleService extends BaseService<SysUserRole> {

    /*新增用户与角色关联关系*/
    void addUserRoleLink(String roleIds,String userId) throws BusinessException;

    /*删除用户与角色关联关系*/
    void delUserRoleLink(String userId) throws BusinessException;

    /*通过用户的id查询关联的角色*/
    List<SysUserRole> userRoleListByUserId(String userId) throws BusinessException;
}