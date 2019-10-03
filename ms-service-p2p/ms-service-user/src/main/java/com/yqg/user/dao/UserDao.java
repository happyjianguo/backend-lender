package com.yqg.user.dao;

import com.yqg.common.dao.BaseDao;
import com.yqg.user.entity.UserUser;
import org.springframework.stereotype.Repository;

/**
 * 用户基本信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Repository
public interface UserDao extends BaseDao<UserUser, String> {

}
