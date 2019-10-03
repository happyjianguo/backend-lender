package com.yqg.system.dao;

import com.yqg.common.dao.BaseDao;
import com.yqg.system.entity.SysPermission;
import org.springframework.stereotype.Repository;

/**
 * 系统权限表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@Repository
public interface SysPermissionDao extends BaseDao<SysPermission, String> {

}
