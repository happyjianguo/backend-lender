package com.yqg.user.dao;

import com.yqg.common.dao.BaseDao;
import com.yqg.user.entity.UsrLoginHistory;
import org.springframework.stereotype.Repository;

/**
 * 用户登录历史表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Repository
public interface UsrLoginHistoryDao extends BaseDao<UsrLoginHistory, String> {

}
