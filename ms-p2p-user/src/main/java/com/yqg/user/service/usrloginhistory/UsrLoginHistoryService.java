package com.yqg.user.service.usrloginhistory;

import com.yqg.api.user.usrloginhistory.ro.UsrLoginHistoryRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.UsrLoginHistory;

/**
 * 用户登录历史表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
public interface UsrLoginHistoryService extends BaseService<UsrLoginHistory> {

    /**
     * 添加pc官网登录记录
     * */
    void addPcLoginHistory(String ipAddress, String userId) throws BusinessException;

    /**
     * 添加登录记录
     * */
    void addLoginHistory(UsrLoginHistoryRo ro) throws BusinessException;
}