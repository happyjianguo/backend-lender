package com.yqg.user.dao;

import com.yqg.common.dao.BaseDao;
import com.yqg.user.entity.UserBank;
import org.springframework.stereotype.Repository;

/**
 * 用户银行卡信息
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Repository
public interface UserBankDao extends BaseDao<UserBank, String> {

}
