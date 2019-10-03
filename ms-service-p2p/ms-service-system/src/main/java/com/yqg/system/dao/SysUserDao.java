package com.yqg.system.dao;

import com.yqg.common.dao.BaseDao;
import com.yqg.system.entity.SysUser;
import org.springframework.stereotype.Repository;

/**
 * 系统用户表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-09-20 15:52:10
 */
@Repository
public interface SysUserDao extends BaseDao<SysUser, String> {

}
