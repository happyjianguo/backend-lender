package com.yqg.user.service.useraccounthistory;

import com.yqg.api.user.useraccounthistory.ro.UserAccounthistoryRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.UserAccounthistory;

/**
 * 用户账户明细表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-11 10:43:36
 */
public interface UserAccounthistoryService extends BaseService<UserAccounthistory> {

    void insterUserAccountHistory(UserAccounthistoryRo userAccounthistoryRo) throws BusinessException;

}