package com.yqg.pay.dao;

import com.yqg.common.dao.BaseDao;
import com.yqg.pay.entity.PayAccountHistory;
import org.springframework.stereotype.Repository;

/**
 * 资金流水表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 17:28:20
 */
@Repository
public interface PayAccountHistoryDao extends BaseDao<PayAccountHistory, String> {

}
