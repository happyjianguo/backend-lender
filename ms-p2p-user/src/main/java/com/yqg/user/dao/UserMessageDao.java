package com.yqg.user.dao;

import com.yqg.common.dao.BaseDao;
import com.yqg.user.entity.UserMessage;
import org.springframework.stereotype.Repository;

/**
 * Created by Lixiangjun on 2019/5/20.
 */
@Repository
public interface UserMessageDao extends BaseDao<UserMessage, String> {
}
