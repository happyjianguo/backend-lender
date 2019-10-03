package com.yqg.user.dao;

import com.yqg.common.dao.BaseDao;
import com.yqg.user.entity.UserAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户账户表
 *
 * @author wu
 * @email wsc@yishufu.com
 * @date 2018-08-31 14:16:46
 */
@Repository
public interface UserAccountDao extends BaseDao<UserAccount, String> {

}
