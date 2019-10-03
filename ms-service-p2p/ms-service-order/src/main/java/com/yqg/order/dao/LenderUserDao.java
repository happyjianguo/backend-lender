package com.yqg.order.dao;

import com.yqg.common.dao.BaseDao;
import com.yqg.order.entity.LenderUser;
import org.springframework.stereotype.Repository;

/**
 * 借款人信息表
 *
 * @author wu
 * @email wu@yishufu.com
 * @date 2018-09-03 11:15:41
 */
@Repository
public interface LenderUserDao extends BaseDao<LenderUser, String> {

}
